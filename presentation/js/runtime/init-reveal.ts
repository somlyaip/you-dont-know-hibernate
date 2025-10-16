import Reveal from 'reveal.js';
import RevealMarkdown from 'reveal.js/plugin/markdown/markdown';
import RevealHighlight from 'reveal.js/plugin/highlight/highlight';

document.addEventListener('DOMContentLoaded', () => {
  console.log('document.DOMContentLoaded');
});

window.addEventListener('load', _ => {
  console.log('window.load');
  // TODO: check it - although more code is presented but code block seems to be smaller
  const zoomMultiplier = 1.2;
  // https://revealjs.com/config/
  Reveal.initialize({
    highlight: {
      highlightOnLoad: false,
    },
    width: Math.round(960 * zoomMultiplier),
    height: Math.round(700 * zoomMultiplier),
    hash: true,
    plugins: [RevealMarkdown, RevealHighlight],
  });
});
