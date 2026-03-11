# Mermaid class diagram

Custom `<mermaid-class-diagram>` web component for rendering Mermaid diagrams with a pre-configured dark theme.

## How it works

1. Takes the inner HTML as raw Mermaid diagram syntax.
2. Wraps it in a `<pre class="mermaid">` element with a front-matter configuration block:
   - Theme: `dark`
   - Dark mode: enabled
   - `useMaxWidth: true`
   - `hideEmptyMembersBox: true`
3. Replaces itself with the configured `<pre>` element.
4. The Mermaid library (imported in `main.ts` as `mermaid/dist/mermaid.esm.min.mjs`) automatically processes all `<pre class="mermaid">` elements on the page.

## Example

```html
<mermaid-class-diagram>
classDiagram
    class Project {
        +Long id
        +String name
    }
    class Issue {
        +Long id
        +String title
    }
    Project "1" --> "*" Issue
</mermaid-class-diagram>
```

## Key files

- `ts/runtime/web-components/mermaid-class-diagram.ts` — web component implementation
- `ts/runtime/main.ts` — Mermaid library import
- `css/styles.css` — diagram container sizing and layout