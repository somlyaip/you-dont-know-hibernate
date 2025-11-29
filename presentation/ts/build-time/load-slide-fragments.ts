import fs from 'node:fs';
import path from 'node:path';
import he from 'he';

/**
 * Loads slide fragments from the slides directory located under the given base directory.
 * - Top-level items must be named with numeric prefixes to define order (e.g., 001-..., 002-...).
 * - Top-level files: .html injected as-is; .md wrapped in <section data-markdown> ... </section>.
 * - Top-level directories (with numeric prefixes) form vertical slide stacks:
 *   - Their child fragments (also with numeric prefixes and .md/.html extensions) are rendered in order
 *   - The children are wrapped in a parent <section> ... </section> to create a vertical slide.
 */
export type TitleReplacementMap = Record<string, string>;

export function loadSlideFragments(baseDir: string, opts?: { titleReplacements?: TitleReplacementMap }): string {
  const slidesDir = path.resolve(baseDir, 'slides');
  if (!fs.existsSync(slidesDir)) {
    return '';
  }

  const directoryEntries = fs
    .readdirSync(slidesDir, { withFileTypes: true })
    .filter((e) => /^(\d+).+/.test(e.name))
    .sort((a, b) => a.name.localeCompare(b.name));

  const parts: string[] = [];

  let topLevelIndex = 0;
  for (const entry of directoryEntries) {
    const fullPath = path.join(slidesDir, entry.name);

    if (entry.isDirectory()) {
      // Vertical slide stack
      const childEntries = fs
        .readdirSync(fullPath, { withFileTypes: true })
        .filter((e) => e.isFile() && /^(\d+).+\.(md|html)$/i.test(e.name))
        .sort((a, b) => a.name.localeCompare(b.name));

      const childParts: string[] = [];
      const dirTitle = toTitleFromFs(entry.name, opts?.titleReplacements);
      // Inject a leading slide for the vertical stack containing only the parent directory as H2
      childParts.push(`<section data-markdown>\n## ${dirTitle}\n</section>`);
      for (const child of childEntries) {
        const childPath = path.join(fullPath, child.name);
        const childContent = fs.readFileSync(childPath, 'utf-8').trim();
        if (child.name.toLowerCase().endsWith('.md')) {
          const fileTitle = toTitleFromFs(child.name, opts?.titleReplacements);
          const updated = ensureVerticalHeadings(childContent, dirTitle, fileTitle);
          childParts.push(`<section data-markdown>\n${updated}\n</section>`);
        } else if (child.name.toLowerCase().endsWith('.html')) {
          const fileTitle = toTitleFromFs(child.name, opts?.titleReplacements);
          const isMeme = /<\s*meme-slide\b/i.test(childContent);
          // If meme slide, ensure the <meme-slide> tag has a title attribute from the file name
          const withMemeTitle = isMeme ? setMemeSlideTitle(childContent, fileTitle) : childContent;
          // New rule: for meme slides inside a directory, do NOT inject dirTitle as H3 (nor H2)
          const withHeadings = isMeme
            ? withMemeTitle // no H2/H3 injection for meme slides inside a vertical stack
            : injectHtmlHeadings(withMemeTitle, { h2: dirTitle, h3: fileTitle });
          childParts.push(escapeSourceCode(withHeadings));
        }
      }
      if (childParts.length > 0) {
        parts.push(`<section>\n${childParts.join('\n\n')}\n</section>`);
      }
    } else if (entry.isFile() && /\.(md|html)$/i.test(entry.name)) {
      // Regular top-level file slide
      const content = fs.readFileSync(fullPath, 'utf-8').trim();
      if (entry.name.toLowerCase().endsWith('.md')) {
        const isFirstTopLevel = topLevelIndex === 0;
        const h2Title = toTitleFromFs(entry.name, opts?.titleReplacements);
        const prefix = isFirstTopLevel ? '' : `## ${h2Title}\n\n`;
        parts.push(`<section data-markdown>\n${prefix}${content}\n</section>`);
      } else {
        const isFirstTopLevel = topLevelIndex === 0;
        const fileTitle = toTitleFromFs(entry.name, opts?.titleReplacements);
        const isMeme = /<\s*meme-slide\b/i.test(content);
        const withMemeTitle = isMeme ? setMemeSlideTitle(content, fileTitle) : content;
        const withHeadings = isFirstTopLevel
          ? withMemeTitle
          : injectHtmlHeadings(withMemeTitle, { h2: fileTitle });
        parts.push(escapeSourceCode(withHeadings));
      }
    }
    topLevelIndex++;
  }

  return parts.join('\n\n');
}

const escapeSourceCode = (html: string): string =>
  html.replace(
    /(<source-code[^>]*>)([\s\S]*?)(<\/source-code>)/gi,
    (match, openTag, inner, closeTag) => {
      if (openTag.includes('file=')) return match;
      // TODO: check this feature: trimColumnsCount
      const escaped = he.encode(inner);
      return `${openTag}${escaped}${closeTag}`;
    },
  );

// Title injection is mandatory (except the very first top-level slide which retains its H1)

/**
 * Remove leading numeric ordering prefix like "001-", "012 ", "03_" from names
 */
function stripNumericPrefix(name: string): string {
  // Only remove the numeric prefix and its separator; do NOT strip extension here.
  // Extension removal must happen exactly once, in toTitleFromFs, using the LAST dot.
  return name.replace(/^[0-9]+[\s._-]*/, '');
}

/**
 * Convert a file/directory name into a title without changing case:
 * - Strip numeric prefix and extension
 * - Replace dashes/underscores with spaces
 * - Collapse multiple spaces and trim
 */
function toTitleFromFs(name: string, replacements?: TitleReplacementMap): string {
  const noPrefix = stripNumericPrefix(name);
  // Keep the original characters as-is; only drop extension
  let base = noPrefix.replace(/\.[^.]+$/, '');
  if (replacements && Object.keys(replacements).length > 0) {
    base = applyReplacements(base, replacements);
  }
  return base.trim();
}

function escapeForRegExp(literal: string): string {
  return literal.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

function applyReplacements(input: string, map: TitleReplacementMap): string {
  let out = input;
  for (const [find, replace] of Object.entries(map)) {
    const re = new RegExp(escapeForRegExp(find), 'g');
    out = out.replace(re, replace);
  }
  return out;
}

/**
 * Ensure vertical markdown slides have proper headings (swapped order):
 * - H2: title from the parent directory (stack title)
 * - H3: title from the child file (slide title)
 * Always prepended before the content.
 */
function ensureVerticalHeadings(content: string, h2Title: string, h3Title: string): string {
  // Always inject H2 (from parent dir) and H3 (from child file) before the content
  const prefix = `## ${h2Title}\n\n### ${h3Title}\n\n`;
  return prefix + content;
}

/**
 * Inject H2 (and optional H3) headings into an HTML slide's markup.
 * - If the HTML contains a <section ...> opening tag, inject right after it.
 * - Otherwise, prepend the headings to the HTML string.
 * Injection is unconditional (except outer logic skips first top-level slide).
 */
function injectHtmlHeadings(html: string, titles: { h2: string; h3?: string }): string {
  // If this is a meme slide, skip injecting H2 entirely
  const hasMemeSlide = /<\s*meme-slide\b/i.test(html);
  const h2Part = hasMemeSlide ? '' : `<h2 ${titles.h3 ? 'class="less-emphasis"' : ''}>${titles.h2}</h2>`;
  const h3Part = titles.h3 ? `\n<h3>${titles.h3}</h3>` : '';
  const headingBlock = `${h2Part}${h3Part}`;
  if (!headingBlock.trim()) {
    // Nothing to inject (e.g., top-level meme-slide without h3)
    return html;
  }
  const sectionOpenRe = /<section(\s[^>]*)?>/i;
  if (sectionOpenRe.test(html)) {
    return html.replace(sectionOpenRe, (m) => `${m}\n  ${headingBlock}`);
  }
  // No <section> wrapper found, prepend headings
  return `${headingBlock}\n${html}`;
}

/**
 * Ensure that a <meme-slide ...> tag carries a title attribute set from the provided title.
 * - Adds title="..." if missing; replaces existing title value if present.
 * - Case-insensitive matching; only affects the first <meme-slide> occurrence.
 */
function setMemeSlideTitle(html: string, title: string): string {
  const re = /<\s*meme-slide\b([\s\S]*?)>/i;
  if (!re.test(html)) return html;
  const escaped = he.encode(title);
  return html.replace(re, (full, attrs: string) => {
    const hasTitle = /\btitle\s*=\s*("[^"]*"|'[^']*'|[^\s>]+)/i.test(attrs);
    let newAttrs: string;
    if (hasTitle) {
      newAttrs = attrs.replace(/\btitle\s*=\s*("[^"]*"|'[^']*'|[^\s>]+)/i, `title="${escaped}"`);
    } else {
      const trimmed = attrs.replace(/\s+$/, '');
      newAttrs = `${trimmed} title="${escaped}"`;
    }
    return full.replace(attrs, newAttrs);
  });
}