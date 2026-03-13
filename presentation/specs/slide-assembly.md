# Slide assembly

Merges individual slide fragments from the `slides/` directory into a single Reveal.js presentation at build time via the `merge-slides-into-index` Vite plugin. Titles are automatically generated from filenames (similar to filesystem-based routing).

## How it works

1. The Vite plugin intercepts the `index.html` transformation.
2. It scans `slides/` for numeric-prefixed files and directories.
3. Top-level `.md` files become horizontal `<section data-markdown>` slides.
4. Top-level `.html` files become horizontal `<section>` slides (their raw HTML is injected).
5. Numeric-prefixed directories become **vertical slide stacks**: a wrapping `<section>` containing each child as a nested `<section>`.
6. The assembled HTML replaces the `<div class="slides">` placeholder in `index.html`.

Ordering is determined by the numeric prefix (e.g. `001`, `012`, `03`).

## Title injection

Titles are extracted from filenames and injected as headings during assembly.

### Title extraction rules

- Strip only the leading numeric prefix and its separator from names (e.g., `012 `, `03_`, `001-`).
- Remove the extension using the last dot, so dots inside the base name are preserved.
- Preserve original characters and casing; no hyphen/underscore to space conversion is performed.
- Apply configurable text chunk replacements after stripping the extension. Default mapping: `" n_" -> "?"` so a filename like `002 Why this provocative title n_.md` injects the title `Why this provocative title?`.
- Configuration: edit `presentation/vite.config.ts` and modify the `titleReplacements` map.

### Heading rules

- **H1** appears only on the very first top-level slide. Its content is untouched.
- **Top-level slides** (not inside a directory): always get an H2 injected from the filename, except the very first slide.
- **Vertical slide stacks** (a numeric-prefixed directory with ordered children):
  - A dedicated first slide is injected at the top of the stack containing only an H2 equal to the directory name (a title slide for the stack).
  - A single stack header is rendered inside the stack wrapper as `<h2 class="stack-header">{directory title}</h2>`. It is hidden on the first (title) slide and visible on subsequent slides.
  - Each child slide gets an H3 from the child filename injected before the original content. The directory title is not repeated on children.
  - For HTML slides inside a stack, the H3 is inserted immediately after the opening `<section>` tag when present; otherwise it is prepended.

### Meme slide exception

HTML slides containing a `<meme-slide>` element receive special treatment:

- No heading (H2 or H3) is injected — neither top-level nor inside a stack.
- Instead, the `<meme-slide>` component receives a `title` attribute derived from the filename. If a `title` attribute already exists, it is overwritten.
- In vertical stacks, the directory intro slide and stack header still appear normally.

### Examples

- Source file: `012 Solve FetchType.EAGER N+1 select problems.md`
  - Injected: `## Solve FetchType.EAGER N+1 select problems` (top-level, not first)
- Source file: `013-every-problem-has-a-solution-1.html` containing `<meme-slide>`
  - No heading injected; `<meme-slide title="every-problem-has-a-solution-1">` is set instead.

## Section reuse

HTML slides can include content from other HTML files:

```html
<section reuse="path/to/shared-slide.html" class="custom-class"></section>
```

The `resolveReuse()` function loads the referenced file, extracts its `<section>` content, and replaces the tag while preserving any extra attributes.

## Inline source code escaping

For `<source-code>` tags without a `file` attribute (inline content), the build step HTML-encodes the inner text using the `he` library. This prevents `<`, `>`, and `&` characters from being interpreted as HTML. Tags with a `file` attribute are left untouched since their content is fetched at runtime.

## Hot module reload

When any file under `slides/` changes during development, both Vite plugins detect the change and trigger a full-page reload via WebSocket.

## Key files

- `ts/build-time/load-slide-fragments.ts` — core merging, title injection, reuse resolution, and escaping logic
- `vite.config.ts` — Vite plugin definitions and `titleReplacements` config
- `index.html` — entry point with `<div class="slides">` placeholder
- `slides/` — source directory for all slide fragments
