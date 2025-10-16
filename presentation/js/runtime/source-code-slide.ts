import Reveal from 'reveal.js';

class SourceCodeSlide extends HTMLElement {
  #codeElement = null;

  constructor() {
      super();
      const template = document.createElement('template');
      // TODO: make font-size and programming language configurable
      const { fromLine, highlightLines, title} = this.getAttributes();
      template.innerHTML = `<section>
        ${title ? `<h2>${title}</h2>` : ''}
        <pre class="language-java">
         <code 
            data-trim
            data-ln-start-from="${fromLine}"
            ${highlightLines ? `data-line-numbers="${highlightLines}"` : 'data-line-numbers'}
            style="font-size: 18px">Loading...</code>
        </pre>
        <button class="force-highlighting-button">&#x21bb;</button>
      </section>`
      // TODO: append design to the ugly button above

      this.#codeElement = template.content.querySelector('code');
      const buttonElement = template.content.querySelector('button');
      buttonElement.addEventListener('click', this.highlightCodeBlock.bind(this));

      this.replaceWith(template.content.firstElementChild);
  }

  connectedCallback() {
    console.log('connectedCallback');
    this.loadAndHighlightSourceCode();
  }

  loadAndHighlightSourceCode(){
    this.loadSourceCodeAsync(this.getAttributes().file).then(_ => {
      console.log(`Source code is loaded from ${this.getAttribute('file')}`);
      if (Reveal.isReady()) {
        this.highlightCodeBlock();
      } else {
        Reveal.on('ready', (_) => {
          this.highlightCodeBlock();
        });
      }
    });
  }

  async loadSourceCodeAsync(file) {
    try {
      const absoluteFilename = `./public/src-to-present/${file}`;
      console.log(`Reading ${absoluteFilename}`);
      const response = await fetch(`${absoluteFilename}?raw`);
      if (!response.ok) {
        // noinspection ExceptionCaughtLocallyJS
        throw new Error(`Failed to fetch file: ${response.statusText}`);
      }
      this.#codeElement.textContent = await this.readRequiredLines(response);
      console.log(`file loaded: ${file}`);
    } catch (error) {
      this.#codeElement.textContent = `Error loading file: ${error.message}`;
    }
  }

  async readRequiredLines(response){
    const { fromLine, toLine } = this.getAttributes();
    console.log(this.getAttributes());

    const text = await response.text();
    const lines = text.split('\n');
    let toLineIndex = lines.length - 1;
    if (toLine) {
      toLineIndex = toLine - 1;
    }
    let content = lines[fromLine - 1]
    for (let i = fromLine; i <= toLineIndex; i++) {
      content += `\n${lines[i]}`;
    }

    return content;
  }

  disconnectedCallback() {
    console.log('disconnectedCallback');
  }

  highlightCodeBlock() {
    console.log('highlightCodeBlock');
    const highlight = Reveal.getPlugin('highlight');
    highlight.highlightBlock(this.#codeElement);
  }

  getAttributes() {
    return {
      file: this.getAttribute("file"),
      fromLine: parseInt(this.getAttribute("from-line")) || 1,
      toLine: parseInt(this.getAttribute("to-line")) || null,
      lines: this.getAttribute("lines"),
      highlightLines: this.getAttribute("highlight-lines"),
      title: this.getAttribute("title"),
    };
  }
}

customElements.define("source-code-slide", SourceCodeSlide);