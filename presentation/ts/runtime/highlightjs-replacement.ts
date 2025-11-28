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

  const html = block.innerHTML;
  const lines = getLines(html); // TODO: do not get html lines as string, use the DOM instead of this

  let table = '<table class="hljs-ln" style="width: 100%; table-layout: fixed; border-collapse: collapse;">';

  lines.forEach((lineHtml, index) => {
    const num = index + 1; // TODO: start line number from the data-ln-start-from attribute

    // Split indentation and content
    const { indent, content } = splitLineIndentation(lineHtml);

    // TODO: move inline styles into css files whenever it's possible
    const rowGridStyle = [
      `display: grid`,
      `grid-template-columns: auto 1fr`,
      `width: 100%`
    ].join(';');

    const indentStyle = 'white-space: pre;';

    const codeStyle = [
      `white-space: pre-wrap`,
      `word-wrap: break-word`,
      `overflow-wrap: break-word`
    ].join(';');

    const safeContent = content.length > 0 ? content : '&nbsp;';

    table += `
      <tr>
        <td class="hljs-ln-numbers" data-line-number="${num}">${num}</td>
        <td class="hljs-ln-code"
        ><div style="${rowGridStyle}">
            <p style="${indentStyle}">${indent}</p>
            <p style="${codeStyle}" class="indented-code">${safeContent}</p>
          </div></td>
      </tr>
    `;
  });

  table += '</table>';
  block.innerHTML = table;
}

function splitLineIndentation(html: string): { indent: string, content: string } {
  // TODO: simplify this - I think indented lines should start with spaces

  // Remove tags to find the leading whitespace in text content
  const textContent = html.replace(/<[^>]+>/g, '');
  const match = textContent.match(/^(\s+)/);
  const indent = match ? match[1] : '';

  if (!indent) {
    return { indent: '', content: html };
  }

  let remainingIndent = indent.length;
  let content = '';

  // Iterate over HTML tokens (tags and text)
  const regex = /<[^>]+>|[^<]+/g;
  let matchArray;
  while ((matchArray = regex.exec(html)) !== null) {
    const token = matchArray[0];
    if (token.startsWith('<')) {
      content += token;
    } else {
      // It's a text node
      if (remainingIndent > 0) {
        if (token.length <= remainingIndent) {
          // This entire text node is part of the indentation
          remainingIndent -= token.length;
        } else {
          // Part of this text node is indentation
          content += token.substring(remainingIndent);
          remainingIndent = 0;
        }
      } else {
        // Indentation fully processed, append rest of text
        content += token;
      }
    }
  }

  return { indent, content };
}

/**
 * Splits HTML into lines, preserving the integrity of open/close tags
 */
function getLines(html: string): string[] {
  const lines: string[] = [];
  const stack: string[] = []; // Stores the full opening tag string: e.g. <span class="x">
  let currentLine = '';

  // Regex to match:
  // Group 1: Closing tag slash (if any)
  // Group 2: Tag name
  // Group 3: Rest of tag attributes
  // OR Group 4: Non-tag text content
  const regex = /<(\/)?([^>\s]+)([^>]*)>|([^<]+)/g;

  let match;
  while ((match = regex.exec(html)) !== null) {
    if (match[4]) {
      // --- Text Content ---
      const text = match[4];
      const parts = text.split(/\r?\n/);

      parts.forEach((part, index) => {
        currentLine += part;

        // If there are more parts, it means we hit a newline
        if (index < parts.length - 1) {
          // 1. Close all currently open tags to finish this line safely
          // Iterate backwards to close inner-most first
          for (let i = stack.length - 1; i >= 0; i--) {
            const tagName = getTagName(stack[i]);
            currentLine += `</${tagName}>`;
          }

          lines.push(currentLine);
          currentLine = '';

          // 2. Re-open all tags for the next line
          for (let i = 0; i < stack.length; i++) {
            currentLine += stack[i];
          }
        }
      });

    } else {
      // --- Tag ---
      const isClosing = match[1] === '/';
      const tagName = match[2].toLowerCase();
      const fullTag = match[0];

      // Self-closing / Void tags don't go on the stack
      // List from: https://developer.mozilla.org/en-US/docs/Glossary/Void_element
      const voidTags = ['area', 'base', 'br', 'col', 'embed', 'hr', 'img', 'input', 'link', 'meta', 'param', 'source', 'track', 'wbr'];

      if (isClosing) {
        // Pop the last tag (assuming well-formed HTML)
        stack.pop();
      } else if (!voidTags.includes(tagName)) {
        stack.push(fullTag);
      }

      currentLine += fullTag;
    }
  }

  if (currentLine) {
    lines.push(currentLine);
  }

  return lines;
}

function getTagName(openTag: string): string {
  // Extract "span" from "<span class=...>"
  const match = openTag.match(/^<([^\s>]+)/);
  return match ? match[1] : 'span';
}

const hljs = { configure, highlightElement, lineNumbersBlock };
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