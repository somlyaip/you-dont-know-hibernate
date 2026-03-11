# Source code display

Custom `<source-code>` web component that displays syntax-highlighted, line-numbered code blocks — either loaded from project source files or provided inline.

## Build-time: auto-copy referenced files

The `copy-required-source-files` Vite plugin scans all `.html` slides for `<source-code file="...">` tags and copies the referenced files (typically Java sources) into `public/src-to-present/`. Paths are resolved relative to the Java project root (`../`). This runs on build start and on hot reload.

## Runtime: the web component

When a `<source-code>` element enters the DOM:

1. If `file` is set, the component fetches the file content from `./src-to-present/{path}`.
2. If no `file` attribute, inline HTML content is used (already escaped at build time).
3. A `<pre><code>` structure is built with appropriate language class and data attributes.
4. The Reveal.js highlight plugin (our Prism adapter) applies syntax highlighting.
5. A toolbar appears above the code with the file path and a refresh (⟲) button.

## Attributes

| Attribute | Description | Default |
|-----------|-------------|---------|
| `file` | Path to source file (relative to project root) | — (uses inline content) |
| `from-line` | First line to display | `1` |
| `to-line` | Last line to display | end of file |
| `highlight-lines` | Lines to highlight (Reveal.js format, e.g. `"3\|5-8"`) | — |
| `language` | Syntax highlighting language | auto-detected from file extension |
| `trim-cols` | Number of leading columns to remove from each line | — |

## Key files

- `ts/runtime/web-components/source-code.ts` — web component implementation
- `ts/build-time/copy-required-source-files.ts` — build-time file copying
- `vite.config.ts` — Vite plugin wiring