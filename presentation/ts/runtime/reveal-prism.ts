import type Reveal from 'reveal.js';
import Prism from 'prismjs';
// Languages
import 'prismjs/components/prism-java';
import 'prismjs/components/prism-sql';
// Plugins
import 'prismjs/plugins/line-highlight/prism-line-highlight';
import 'prismjs/plugins/line-numbers/prism-line-numbers';

// Minimal Reveal.js plugin wrapper around Prism.js
// Name: RevealPrism
// Features:
// - Highlights <pre><code class="language-*"></code></pre>
// - Supports Prism line-numbers and line-highlight via attributes/classes
// - Optional soft wrap with hanging indent

export type RevealPrismOptions = {
  // Enable soft wrapping of code lines
  softWrap?: boolean;
  // Hanging indent width for wrapped lines (CSS ch units)
  wrapIndentCh?: number; // default: 2
};

function trimCodeIfRequested(codeEl: HTMLElement) {
  if (codeEl.hasAttribute('data-trim')) {
    // Remove common leading indentation
    const lines = (codeEl.textContent || '').replace(/\s+$/g, '').split('\n');
    // Find smallest indent (spaces or tabs) across non-empty lines
    let minIndent: number | null = null;
    for (const line of lines) {
      if (!line.trim()) continue;
      const m = line.match(/^(\s*)/);
      const indent = m ? m[1].length : 0;
      if (minIndent === null || indent < minIndent) minIndent = indent;
    }
    if (minIndent && minIndent > 0) {
      codeEl.textContent = lines.map(l => l.slice(Math.min(minIndent!, l.length))).join('\n');
    } else {
      codeEl.textContent = lines.join('\n');
    }
  }
}

function ensureLanguage(codeEl: HTMLElement) {
  // Default to language-none if no language-* class exists
  if (![...codeEl.classList].some(c => c.startsWith('language-'))) {
    codeEl.classList.add('language-none');
  }
}

function applySoftWrap(preEl: HTMLElement, options?: RevealPrismOptions) {
  const { softWrap = false, wrapIndentCh = 2 } = options || {};
  if (!softWrap) return;
  preEl.classList.add('reveal-prism-wrap');
  preEl.style.setProperty('--reveal-prism-wrap-indent-ch', String(wrapIndentCh));
}

function highlightElement(codeEl: Element, options?: RevealPrismOptions) {
  const pre = codeEl.parentElement as HTMLElement | null;
  if (pre?.tagName.toLowerCase() === 'pre') {
    applySoftWrap(pre!, options);
  }
  const el = codeEl as HTMLElement;
  trimCodeIfRequested(el);
  ensureLanguage(el);
  Prism.highlightElement(el);
}

function highlightAllWithin(root: Element, options?: RevealPrismOptions) {
  const blocks = root.querySelectorAll('pre code');
  blocks.forEach(el => highlightElement(el, options));
}

const RevealPrism = {
  id: 'RevealPrism',
  init: function (reveal: typeof Reveal, options?: RevealPrismOptions) {
    const deck: any = reveal;

    // Public API similar to RevealHighlight
    const api = {
      highlightBlock: (el: Element) => highlightElement(el, options),
      highlightBlocks: (root: Element) => highlightAllWithin(root, options),
    };

    // Initial highlight when deck is ready
    deck.on('ready', (evt: any) => {
      const root = deck.getRevealElement?.() ?? document;
      highlightAllWithin(root, options);
    });

    // Re-highlight on slide change and when fragments are shown/hidden
    const rehighlightCurrent = () => {
      const current = deck.getCurrentSlide?.();
      if (current) highlightAllWithin(current, options);
    };

    deck.on('slidechanged', rehighlightCurrent);
    deck.on('fragmentshown', rehighlightCurrent);
    deck.on('fragmenthidden', rehighlightCurrent);

    // Expose API for external usage
    return api;
  },
};

export default RevealPrism;
