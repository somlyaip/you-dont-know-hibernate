class MermaidClassDiagram extends HTMLElement {
  constructor() {
    super();
    this.processContent();
  }

  private processContent(): void {
    const originalContent = this.innerHTML || '';
    
    // Create a plain pre element
    const preElement = document.createElement('pre');
    preElement.classList.add('mermaid');
    preElement.innerHTML = `
    ---
    config:
      theme: dark
      darkMode: true
      class:
        useMaxWidth: true
        hideEmptyMembersBox: true
    ---
    ${originalContent.trim()}
    `;

    // Replace the current element's content
    this.innerHTML = '';
    this.appendChild(preElement);
  }
}

customElements.define('mermaid-class-diagram', MermaidClassDiagram);
