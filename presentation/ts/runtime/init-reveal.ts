import Reveal from 'reveal.js';
import RevealMarkdown from 'reveal.js/plugin/markdown/markdown';
import RevealPrism from './reveal-prism';

document.addEventListener('DOMContentLoaded', (_evt: Event) => {
  console.log('document.DOMContentLoaded');
});

window.addEventListener('load', (_evt: Event) => {
  console.log('window.load');
  // TODO: check it - although more code is presented but code block seems to be smaller
  const zoomMultiplier = 1.2;
  // https://revealjs.com/config/
  (Reveal as any).initialize({
    width: Math.round(960 * zoomMultiplier),
    height: Math.round(700 * zoomMultiplier),
    hash: true,
    plugins: [RevealMarkdown, RevealPrism],
    RevealPrism: {
      softWrap: true,
      wrapIndentCh: 2,
    },
  });
});
