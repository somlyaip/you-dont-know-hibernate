# Meme slide

Custom `<meme-slide>` web component that creates a full-screen background image slide with a title overlay and photo credits.

## How it works

The component constructs a Reveal.js `<section>` with:
- `data-background-image` set to the provided image URL
- An `<h2 class="more-contrast">` overlay with the slide title
- An `<aside class="notes">` containing credits (visible in speaker notes)

It then replaces itself with the constructed section element.

## Attributes

| Attribute | Description | Default |
|-----------|-------------|---------|
| `img` | Background image URL (required) | — |
| `title` | Title displayed as overlay | auto-filled from filename (see filesystem-based-title-creation) |
| `credits` | Credits link placed in speaker notes | — |
| `bg-size` | CSS `background-size` value | `cover` |

## Title injection

When a slide file contains a `<meme-slide>` element, the filesystem-based title creation system skips heading injection and instead sets the `title` attribute from the filename. See `filesystem-based-title-creation.md` for details.

## Example

```html
<meme-slide
  img="https://example.com/meme.jpg"
  credits="https://example.com/source"
  bg-size="contain">
</meme-slide>
```

## Key files

- `ts/runtime/web-components/meme-slide.ts` — web component implementation
- `ts/build-time/load-slide-fragments.ts` — title attribute injection logic
- `css/styles.css` — `.more-contrast` styling