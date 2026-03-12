import { JSDOM } from 'jsdom';
import { beforeEach, describe, expect, it } from 'vitest';

import hljs from './highlightjs-replacement';

describe('highlightjs-replacement', () => {
  const normalizeHtml = (html: string) => html.replace(/>\s+</g, '><').trim();

  let document: Document;

  beforeEach(() => {
    const dom = new JSDOM('<!DOCTYPE html>');
    document = dom.window.document;
    global.document = document;
    global.window = dom.window as any;
    global.HTMLElement = dom.window.HTMLElement;
    global.Node = dom.window.Node;
  });

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
    hljs.highlightElement(code);

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

  it('should correctly indent and split Java multiline strings', () => {
    const codeSnippet = `assertThatSqlStatements(() -> {
    List<Issue> issues = entityManager.createQuery("""
            SELECT i FROM Issue i
            WHERE i.title = :title
            """, Issue.class)
        .setParameter("title", "Bug 1")
        .getResultList();

    assertThat(issues).hasSize(1);
}).hasSelectCount(2);`;

    const pre = document.createElement('pre');
    pre.className = 'language-java';
    const codeBlock = document.createElement('code');
    // Mimic how HTML would look before highlighting (escaped entities)
    codeBlock.innerHTML = codeSnippet.replace(/</g, '&lt;').replace(/>/g, '&gt;');
    pre.appendChild(codeBlock);
    document.body.appendChild(pre);

    // Apply highlighting
    hljs.highlightElement(codeBlock);

    // Extract lines
    const lines = hljs.getIndentsAndContents(codeBlock);

    // Debug output to see what happened
    console.log('Generated lines:', lines);

    // Assertions based on the visual expectation of the snippet
    // 1. assertThatSqlStatements...
    // 2.     List<Issue>...
    // 3.             SELECT...
    // 4.             WHERE...
    // 5.             """, Issue.class)
    // 6.         .setParameter...
    // 7.         .getResultList();
    // 8. (Empty line)
    // 9.     assertThat...
    // 10. }).hasSelectCount(2);

    expect(lines).toHaveLength(10);

    expect(lines[0].content).toContain('assertThatSqlStatements');
    expect(lines[0].indent).toBe('');

    expect(lines[1].content).toContain('createQuery');
    expect(lines[1].content).toContain('"""');
    expect(lines[1].indent).toBe('    ');

    // This is the critical part: checking if the multiline string was split correctly
    expect(lines[2].content).toContain('SELECT i FROM Issue i');
    expect(lines[2].indent).toBe('            ');

    expect(lines[3].content).toContain('WHERE i.title = :title');
    expect(lines[3].indent).toBe('            ');

    expect(lines[4].content).toContain('"""');
    expect(lines[4].content).toContain('Issue');
    expect(lines[4].indent).toBe('            ');

    expect(lines[5].content).toContain('setParameter');
    expect(lines[5].content).toContain('Bug 1');
    expect(lines[5].indent).toBe('        ');

    expect(lines[6].content).toContain('getResultList');
    expect(lines[6].indent).toBe('        ');

    expect(lines[7].content).toBe('');
    expect(lines[7].indent).toBe('');

    expect(lines[8].content).toContain('assertThat');
    expect(lines[8].content).toContain('hasSize');
    expect(lines[8].indent).toBe('    ');

    expect(lines[9].content).toContain('hasSelectCount');
    expect(lines[9].indent).toBe('');
  });
});
