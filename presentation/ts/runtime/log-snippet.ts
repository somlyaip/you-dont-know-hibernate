class LogSnippet extends HTMLElement {
  constructor() {
    super();
    this.processContent();
  }

  private processContent(): void {
    const originalContent = this.innerHTML || '';
    const trimmedContent = this.trimAndPreserveOriginalIntendOfLogLines(originalContent);
    
    // Create a plain pre element
    const preElement = document.createElement('pre');
    preElement.innerHTML = trimmedContent;
    // use class hljs to copy background and color rules of the source codes
    preElement.classList.add('log-snippet-pre', 'hljs');

    const divElement = document.createElement('div');
    const title = this.getAttribute('title');
    if (title) {
      const titleElement = document.createElement('span');
      titleElement.textContent = title;
      divElement.appendChild(titleElement);
    }

    divElement.appendChild(preElement);

    // Replace the current element's content
    this.innerHTML = '';
    this.appendChild(divElement);
  }

  private trimAndPreserveOriginalIntendOfLogLines(content: string): string {
    let lines = content.split('\n');
    let firstNonEmptyLineIndex = 0;
    while (!lines[firstNonEmptyLineIndex].trim()) {
      firstNonEmptyLineIndex++;
    }
    let lastNonEmptyLineIndex = lines.length - 1;
    while (!lines[lastNonEmptyLineIndex].trim()) {
      lastNonEmptyLineIndex--;
    }
    lines = lines.slice(firstNonEmptyLineIndex, lastNonEmptyLineIndex + 1);

    // Index of the first non-empty char in the first line
    const intend = lines[0].indexOf(lines[0].trim()[0]);
    const linesWithOriginalIntend = lines.map(line => line.slice(intend));
    return linesWithOriginalIntend.join('\n');
  }
}

customElements.define('log-snippet', LogSnippet);
