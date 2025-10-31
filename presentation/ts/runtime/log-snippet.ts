class LogSnippet extends HTMLElement {
  constructor() {
    super();
    this.processContent();
  }

  private processContent(): void {
    const originalContent = this.textContent || '';
    const trimmedContent = this.trimLogLines(originalContent);
    
    // Create a plain pre element
    const preElement = document.createElement('pre');
    preElement.textContent = trimmedContent;
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

  private trimLogLines(content: string): string {
    const lines = content.split('\n');
    const trimmedLines = lines.map(line => line.trim());
    let firstNonEmptyLineIndex = 0;
    while (!trimmedLines[firstNonEmptyLineIndex]) {
      firstNonEmptyLineIndex++;
    }
    return trimmedLines.slice(firstNonEmptyLineIndex).join('\n');
  }
}

customElements.define('log-snippet', LogSnippet);
