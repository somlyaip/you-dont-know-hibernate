import { refractor } from 'refractor'
import java from 'refractor/java';
import javascript from 'refractor/javascript';
import typescript from 'refractor/typescript';
import json from 'refractor/json';
import css from 'refractor/css';
import yaml from 'refractor/yaml';
import he from 'he';

// This file mocks highlight.js for reveal.js

refractor.register(java);
refractor.register(javascript);
refractor.register(typescript);
refractor.register(json);
refractor.register(css);
refractor.register(yaml);

// ESM-compatible mock for highlight.js API that reveal.js expects
export function configure(options: any) {
  console.debug('Mock HighlightJS: configure called', options);
}

export function highlightElement(codeBlock: HTMLElement) {
  console.debug('Mock HighlightJS: highlightElement called for', codeBlock);
  console.log(codeBlock.innerHTML);
  
  // The language is on the <pre> tag, which is the parent of <code>
  const preElement = codeBlock.parentElement;
  let language = 'plaintext'; // Default language
  if (preElement) {
    const languageClass = Array.from(preElement.classList).find(cls => cls.startsWith('language-'));
    if (languageClass) {
      language = languageClass.replace('language-', '');
    }
  }

  // Ensure the main block has the 'hljs' class for theme background/color
  codeBlock.classList.add('hljs');

  // refractor.highlight expects decoded code.
  // It also doesn't handle <br> tags, so we need to replace them with newlines.
  const decodedHtml = he.decode(codeBlock.innerHTML.replace(/<br\s*\/?>/gi, '\n'));

  const tree = refractor.registered(language)
    ? refractor.highlight(decodedHtml, language)
    : refractor.highlight(decodedHtml, 'plaintext');

  // The result of refractor.highlight is a HAST tree.
  // We need to convert it to a DOM tree, translating Prism-style classes
  // to Highlight.js-style classes.
  codeBlock.innerHTML = '';
  if (tree.children) {
    for (const child of tree.children) {
      hastToDom(child, codeBlock);
    }
  }
}

function hastToDom(node: any, parent: HTMLElement) {
  if (node.type === 'text') {
    parent.appendChild(document.createTextNode(node.value));
    return;
  }

  if (node.type === 'element') {
    const el = document.createElement(node.tagName);
    if (node.properties && node.properties.className) {
      const prismClasses = node.properties.className as string[];
      // In Prism, tokens have a 'token' class and a class for the token type.
      // e.g., ['token', 'keyword']
      // In Highlight.js, it's just 'hljs-keyword'.

      // prism -> hljs
      const replacements: Record<string, string> = {
        'class-name': 'title class_',
        'annotation': 'meta',
        'key': 'attr',
        'boolean': 'literal',
        'important': 'strong',
        // Extend highlight.js functionality
        'function': 'attr',
      };

      const classesToIgnore = [
        'atrule'
      ];

      const prefixLength = 'hljs-'.length;
      const hljsClasses = prismClasses
        .filter(cls => cls !== 'token')
        .map(cls => {
           const replacement = replacements[cls];
           return replacement ? `hljs-${replacement}` : `hljs-${cls}`;
        })
        .filter(cls => !classesToIgnore.includes(cls.substring(prefixLength)));

      if (hljsClasses.length > 0) {
        el.className = hljsClasses.join(' ');
      }
    }

    parent.appendChild(el);

    if (node.children) {
      for (const child of node.children) {
        hastToDom(child, el);
      }
    }
  }
}

export function lineNumbersBlock(block: HTMLElement, config: any = {}) {
  console.debug('Mock HighlightJS: lineNumbersBlock called for', block, config);

  let table = '<table class="hljs-ln" style="width: 100%; table-layout: fixed; border-collapse: collapse;">';
  const startFrom = block.hasAttribute('data-ln-start-from')
      ? parseInt(block.getAttribute('data-ln-start-from') as string, 10)
      : 1;

  const lines: { indent: string; content: string }[] = getIndentsAndContents(block);
  lines.forEach((line, index) => {
    const num = index + startFrom;
    const { indent, content } = line;

    const safeContent = content.length > 0 ? content : '&nbsp;';

    table += `
      <tr>
        <td class="hljs-ln-numbers" data-line-number="${num}">${num}</td>
        <td class="hljs-ln-code">
          <div class="hljs-ln-row">
            <p class="hljs-ln-indent">${indent}</p>
            <p class="hljs-ln-line-content indented-code">${safeContent}</p>
          </div>
        </td>
      </tr>
    `;
  });

  table += '</table>';
  block.innerHTML = table;
}

function getIndentsAndContents(block:HTMLElement){
  const lines: { indent: string; content: string }[] = [];
  let currentIndent = '';
  let currentContent = '';
  let isCapturingIndent = true;

  const commitLine = () => {
    lines.push({ indent: currentIndent, content: currentContent });
    currentIndent = '';
    currentContent = '';
    isCapturingIndent = true;
  };

  const escapeHtml = (str: string) => str
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;');

  block.childNodes.forEach((node) => {
    if (node.nodeType === Node.TEXT_NODE) {
      const text = node.textContent || '';
      const parts = text.split(/\r\n|\r|\n/g);

      parts.forEach((part, index) => {
        if (index > 0) {
          commitLine();
        }

        if (isCapturingIndent) {
          const match = part.match(/^(\s*)(.*)/);
          if (match) {
            currentIndent += match[1];
            if (match[2].length > 0) {
              currentContent += escapeHtml(match[2]);
              isCapturingIndent = false;
            }
          }
        } else {
          currentContent += escapeHtml(part);
        }
      });
    } else if (node.nodeType === Node.ELEMENT_NODE) {
      const el = node as HTMLElement;
      const textContent = el.textContent || '';

      if (el.children.length === 0 && textContent.includes('\n')) {
        const parts = textContent.split(/\r\n|\r|\n/g);
        parts.forEach((part, index) => {
          if (index > 0) {
            commitLine();
          }

          if (isCapturingIndent) {
            const match = part.match(/^(\s*)(.*)/);
            if (match) {
              currentIndent += match[1];
              const content = match[2];
              if (content.length > 0) {
                const clone = el.cloneNode(false) as HTMLElement;
                clone.textContent = content;
                currentContent += clone.outerHTML;
                isCapturingIndent = false;
              }
            }
          } else {
            if (part.length > 0) {
              const clone = el.cloneNode(false) as HTMLElement;
              clone.textContent = part;
              currentContent += clone.outerHTML;
            }
          }
        });
      } else {
        if (isCapturingIndent) {
          isCapturingIndent = false;
        }
        currentContent += el.outerHTML;
      }
    }
  });
  commitLine();

  // Remove last empty line if it was created by a trailing newline
  if (lines.length > 0) {
    const last = lines[lines.length - 1];
    if (!last.indent && !last.content) {
      lines.pop();
    }
  }
  return lines;
}

const hljs = { configure, highlightElement, lineNumbersBlock, getIndentsAndContents };
// We want to intercept writes to 'lineNumbersBlock'
const ourLineNumbersBlock = hljs.lineNumbersBlock;

Object.defineProperty(hljs, 'lineNumbersBlock', {
  get: () => {
    // Always return our implementation
    return ourLineNumbersBlock;
  },
  set: (newValue) => {
    // Silently ignore any attempt to overwrite it
    console.debug('An attempt to overwrite lineNumbersBlock was ignored.');
  },
  enumerable: true,
  configurable: true,
});
console.log('hljs.lineNumbersBlock is defined to writable: false');
export default hljs;