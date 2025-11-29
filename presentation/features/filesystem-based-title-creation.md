# Filesystem-based title creation

This feature generates presentation slide titles from their filesystem names (similar to filesystem-based routing). Ordering is controlled by numeric prefixes in file and directory names, which are removed from titles.

Rules:
- H1 appears only on the very first top‑level slide. Its content is untouched.
- Top‑level slides (Markdown or HTML, not inside a directory):
  - Always get an H2 injected from the source filename (after removing the numeric prefix and the extension), except the very first slide.
 - Vertical slide stacks (a numeric‑prefixed directory with ordered children):
  - A new leading slide is injected at the top of the stack containing only an H2 equal to the directory name.
  - Each child slide (Markdown or HTML) in the stack gets an H3 from the child filename injected before the original content. The directory title is not repeated on children.
    - For HTML slides inside a stack, the H3 is inserted immediately after the opening <section> tag when present; otherwise it is prepended to the HTML.
    - Exception: If the HTML slide contains a <meme-slide ...> element, do NOT inject any heading for that child (no H2, no H3). The meme slide will only receive a title attribute (see below).
  
Exceptions:
 - HTML slides that contain a <meme-slide ...> element:
  - Top‑level: no heading is injected.
  - Inside a vertical stack: no heading is injected (neither H2 nor H3). The directory intro slide still appears at the start of the stack.
  - Additionally, the meme-slide component receives a title attribute derived from the source filename (after numeric prefix removal, last‑dot extension removal, and applying replacements). If a title attribute already exists, it is overwritten. This applies to both top‑level and vertical slides.

Title extraction:
- Strip only the leading numeric prefix and its separator from names (e.g., `012 `, `03_`, `001-`).
- Remove the extension using the last dot, so dots inside the base name are preserved.
- Preserve original characters and casing; no hyphen/underscore to space conversion is performed.
- Apply configurable text chunk replacements after stripping the extension. Default mapping: `" n_" -> "?"` so a filename like `002 Why this provocative title n_.md` injects the title `Why this provocative title?`.

Configuration:
- Edit `presentation/vite.config.ts` and modify the `titleReplacements` map to add more replacements as needed.

Example:
- Source file: `012 Solve FetchType.EAGER N+1 select problems.md`
  - Injected: `## Solve FetchType.EAGER N+1 select problems` (for a top‑level slide that is not the first).
  
HTML example:
- Source file: `013-every-problem-has-a-solution-1.html`
  - Injected into HTML (if top‑level and not the first): `<h2>every-problem-has-a-solution-1</h2>` will be added; in a vertical stack, `<h2>` (parent dir) is followed by `<h3>` (child filename).
  - If the file contains a `<meme-slide>` element, then no `<h2>` is injected. In a vertical stack, only `<h3>` (parent dir) is injected; for top‑level such slides, nothing is injected. In all meme-slide cases, the `<meme-slide>` tag will have `title="..."` set from the filename-based title.
