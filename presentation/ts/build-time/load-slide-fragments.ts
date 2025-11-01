import fs from 'node:fs';
import path from 'node:path';

/**
 * Loads slide fragments from the slides directory located under the given base directory.
 * - Files must be named with numeric prefixes to define order (e.g., 001-..., 002-...).
 * - .html files are injected as-is.
 * - .md files are wrapped in <section data-markdown> ... </section>.
 */
export function loadSlideFragments(baseDir: string): string {
  const slidesDir = path.resolve(baseDir, 'slides');
  if (!fs.existsSync(slidesDir)) {
    return '';
  }

  const files = fs
    .readdirSync(slidesDir)
    .filter((f) => /^(\d+).+\.(md|html)$/i.test(f))
    .sort((a, b) => a.localeCompare(b));

  const parts: string[] = [];
  for (const file of files) {
    const fullPath = path.join(slidesDir, file);
    const content = fs.readFileSync(fullPath, 'utf-8');
    if (file.toLowerCase().endsWith('.md')) {
      parts.push(`<section data-markdown>\n${content.trim()}\n</section>`);
    } else if (file.toLowerCase().endsWith('.html')) {
      parts.push(content.trim());
    }
  }
  return parts.join('\n\n');
}
