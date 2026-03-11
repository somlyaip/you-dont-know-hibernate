# You don't know Hibernate - Presentation

## Setting up
1. Install node.js
    1. Download and install node.js v22
    1. Or
        1. download and install nvm
        1. `cd ./presentation`
        1. `nvm use`
1. If you are currently not in the `presentation` subdirectory: `cd ./presentation` 
1. `npm ci`

## Running in development mode
1. Run from your terminal: `npm run dev`
1. In the terminal where the `npm run dev` command is running: 
press the 'o' and after that the 'Enter' key to open the presentation in your default browser

Note: index.html references TypeScript modules (via <script type="module" src="...*.ts">). This works when served by Vite (dev/build/preview). Opening the HTML file directly from the filesystem will not work because browsers cannot execute TypeScript without a bundler.

## TypeScript
This module now uses TypeScript for both browser runtime and Node build-time utilities.
- Runtime code lives under `js/runtime/*.ts` and is bundled by Vite for the browser.
- Build-time utilities live under `js/build-time/*.ts` and execute in the Node.js process via Vite's config plugin.

### Build
- `npm run build` — builds the browser bundle (runtime). During build, the Node-side plugin copies the referenced source files into `public/src-to-present/`.
- `npm run preview` — serves the built presentation for preview.

## Reveal.js
This presentation uses [Reveal.js](https://revealjs.com).
If you are not familiar with it, check out its [demo](https://revealjs.com/?demo).

## Feature specs
Detailed documentation of the presentation's custom features lives in [`specs/`](./specs/):
- [Slide assembly](./specs/slide-assembly.md) — build-time merging of slide fragments, title injection, section reuse, HMR
- [Prism adapter for Reveal.js highlight](./specs/prism-adapter-for-revealjs-highlight.md) — Prism.js highlighting via a highlight.js adapter
- [Source code display](./specs/source-code-display.md) — `<source-code>` web component with auto-copy from project sources
- [Log snippet](./specs/log-snippet.md) — `<log-snippet>` web component for formatted log output
- [Meme slide](./specs/meme-slide.md) — `<meme-slide>` web component for full-screen image slides
- [Mermaid class diagram](./specs/mermaid-class-diagram.md) — `<mermaid-class-diagram>` web component with dark theme
