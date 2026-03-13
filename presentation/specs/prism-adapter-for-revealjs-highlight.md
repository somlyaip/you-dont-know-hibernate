# Prism adapter for Reveal.js highlight

Reveal.js's built-in highlight plugin requires highlight.js, but highlight.js has limited Java and SQL syntax highlighting. This feature replaces the highlighting engine with Prism.js (via refractor) while keeping Reveal.js's plugin unchanged.

## How it works

A Vite module alias intercepts all `import 'highlight.js'` calls — including those inside Reveal.js internals — and redirects them to a custom adapter at `ts/runtime/highlightjs-replacement.ts`.

The adapter:

1. Implements the highlight.js API that Reveal.js expects (`highlightElement`, `lineNumbersBlock`, `configure`).
2. Delegates actual syntax highlighting to **refractor**, the AST-based engine behind Prism.js.
3. Translates Prism token CSS classes (e.g. `token keyword`) to highlight.js classes (e.g. `hljs-keyword`) so existing `hljs-*` CSS themes work seamlessly.
4. Provides a custom line numbers implementation that replaces the standard highlight.js one.

Reveal.js has no awareness it is not using the real highlight.js.

## Key files

- `ts/runtime/highlightjs-replacement.ts` — the adapter (highlight.js mock + refractor integration)
- `ts/runtime/highlightjs-replacement.test.ts` — tests for highlighting and line number generation
- `vite.config.ts` — the `resolve.alias` that wires it all together (redirects `highlight.js` imports)

## Registered languages

Java, JavaScript, TypeScript, JSON, CSS, YAML. To add a new language, import it from `refractor/<lang>` and call `refractor.register(...)` in the adapter.

## Class name mapping

Prism and highlight.js use different class naming conventions. The adapter translates them:

| Prism class  | highlight.js class  |
| ------------ | ------------------- |
| `class-name` | `hljs-title class_` |
| `annotation` | `hljs-meta`         |
| `key`        | `hljs-attr`         |
| `boolean`    | `hljs-literal`      |
| `important`  | `hljs-strong`       |
| `function`   | `hljs-attr`         |
| `keyword`    | `hljs-keyword`      |
| _(others)_   | `hljs-{class}`      |

## Override protection

The `lineNumbersBlock` function is defined with `Object.defineProperty` (getter/setter) to prevent external code from overwriting it at runtime.
