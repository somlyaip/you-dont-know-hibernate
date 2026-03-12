// Vite entry for the presentation runtime.
// Import order matters: define custom element before initializing Reveal.
import './web-components/source-code.ts';
import './web-components/log-snippet.ts';
import './web-components/mermaid-class-diagram';
import './web-components/meme-slide';
import './init-reveal.ts';
import 'mermaid/dist/mermaid.esm.min.mjs';
