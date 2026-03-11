# Log snippet

Custom `<log-snippet>` web component for displaying formatted log output or console text.

## How it works

1. Takes the inner HTML as log content.
2. Trims leading and trailing empty lines.
3. Calculates the indentation of the first non-empty line and removes that baseline from all lines (normalizes indentation).
4. Wraps the result in a `<pre class="log-snippet-pre hljs">` block for consistent styling with code blocks.

## Attributes

| Attribute | Description | Default |
|-----------|-------------|---------|
| `title` | Optional title displayed above the log | — |

## Example

```html
<log-snippet title="Hibernate SQL output">
    select i1_0.id, i1_0.description, p1_0.id, p1_0.name
    from issue i1_0
             left join project p1_0 on p1_0.id = i1_0.project_id
</log-snippet>
```

## Key files

- `ts/runtime/web-components/log-snippet.ts` — web component implementation
- `css/styles.css` — styling (`.log-snippet-pre`, flexbox layout)