import Reveal from 'reveal.js';
import RevealMarkdown from 'reveal.js/plugin/markdown/markdown';
import RevealHighlight from 'reveal.js/plugin/highlight/highlight';
import RevealNotes from 'reveal.js/plugin/notes/notes.js';

document.addEventListener('DOMContentLoaded', (_evt: Event) => {
  console.log('document.DOMContentLoaded');
});

window.addEventListener('load', (_evt: Event) => {
  console.log('window.load');
  // TODO: check it - although more code is presented but code block seems to be smaller
  const zoomMultiplier = 1.2;
  // https://revealjs.com/config/
  (Reveal as any).initialize({
    highlight: {
      highlightOnLoad: false,
    },
    width: Math.round(960 * zoomMultiplier),
    height: Math.round(700 * zoomMultiplier),
    hash: true,
    plugins: [RevealMarkdown, RevealHighlight, RevealNotes],
  });
});
