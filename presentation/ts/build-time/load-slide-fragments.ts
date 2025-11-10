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
export function loadSlideFragments(baseDir: string): string {
  const slidesDir = path.resolve(baseDir, 'slides');
  if (!fs.existsSync(slidesDir)) {
    return '';
  }

  const directoryEntries = fs
    .readdirSync(slidesDir, { withFileTypes: true })
    .filter((e) => /^(\d+).+/.test(e.name))
    .sort((a, b) => a.name.localeCompare(b.name));

  const parts: string[] = [];

  for (const entry of directoryEntries) {
    const fullPath = path.join(slidesDir, entry.name);

    if (entry.isDirectory()) {
      // Vertical slide stack
      const childEntries = fs
        .readdirSync(fullPath, { withFileTypes: true })
        .filter((e) => e.isFile() && /^(\d+).+\.(md|html)$/i.test(e.name))
        .sort((a, b) => a.name.localeCompare(b.name));

      const childParts: string[] = [];
      for (const child of childEntries) {
        const childPath = path.join(fullPath, child.name);
        const childContent = fs.readFileSync(childPath, 'utf-8').trim();
        if (child.name.toLowerCase().endsWith('.md')) {
          childParts.push(`<section data-markdown>\n${childContent}\n</section>`);
        } else if (child.name.toLowerCase().endsWith('.html')) {
          childParts.push(escapeSourceCode(childContent));
        }
      }
      if (childParts.length > 0) {
        parts.push(`<section>\n${childParts.join('\n\n')}\n</section>`);
      }
    } else if (entry.isFile() && /\.(md|html)$/i.test(entry.name)) {
      // Regular top-level file slide
      const content = fs.readFileSync(fullPath, 'utf-8').trim();
      if (entry.name.toLowerCase().endsWith('.md')) {
        parts.push(`<section data-markdown>\n${content}\n</section>`);
      } else {
        parts.push(escapeSourceCode(content));
      }
    }
  }

  return parts.join('\n\n');
}

const escapeSourceCode = (html: string): string =>
  html.replace(
    /(<source-code[^>]*>)([\s\S]*?)(<\/source-code>)/gi,
    (match, openTag, inner, closeTag) => {
      if (openTag.includes('file=')) return match;
      const escaped = he.encode(inner);
      return `${openTag}${escaped}${closeTag}`;
    },
  );