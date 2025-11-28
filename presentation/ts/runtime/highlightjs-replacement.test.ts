import { describe, it, expect } from 'vitest';
import { highlightElement } from './highlightjs-replacement';

describe('highlightjs-replacement', () => {
  const normalizeHtml = (html: string) => html.replace(/>\s+</g, '><').trim();

  it('highlightElement highlights YAML structure correctly', () => {
    // Arrange: Create the DOM structure mimicking the provided HTML
    const pre = document.createElement('pre');
    pre.classList.add('language-yaml', 'code-wrapper');

    const code = document.createElement('code');
    code.setAttribute('data-trim', '');
    code.setAttribute('data-ln-start-from', '1');
    code.setAttribute('data-line-numbers', '');

    // Set the content as per the example
    code.innerHTML = `spring:
  jpa:
    properties:
      hibernate:
        generate_statistics: true`;

    pre.appendChild(code);

    // Act
    highlightElement(code);

    // Assert
    const expectedHtml = `<code data-trim="" data-ln-start-from="1" data-line-numbers="" class="hljs">
      <span class="hljs-attr">spring</span><span class="hljs-punctuation">:</span>
      <span class="hljs-attr">jpa</span><span class="hljs-punctuation">:</span>
      <span class="hljs-attr">properties</span><span class="hljs-punctuation">:</span>
      <span class="hljs-attr">hibernate</span><span class="hljs-punctuation">:</span>
      <span class="hljs-attr">generate_statistics</span><span class="hljs-punctuation">:</span>
      <span class="hljs-literal hljs-strong">true</span>
    </code>`;

    expect(normalizeHtml(code.outerHTML)).toBe(normalizeHtml(expectedHtml));
  });
});
