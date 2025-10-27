import Reveal from 'reveal.js';

type Attributes = {
  file: string;
  fromLine: number;
  toLine: number | null;
  lines: string | null;
  highlightLines: string | null;
  title: string | null;
};

class SourceCodeSlide extends HTMLElement {
  #codeElement: HTMLElement | null = null;

  constructor() {
    super();
    const template = document.createElement('template');
    // TODO: make font-size and programming language configurable
    const { fromLine, highlightLines, title } = this.getAttributes();
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
      </section>`;
    // TODO: append design to the ugly button above

    this.#codeElement = template.content.querySelector('code') as HTMLElement | null;
    const buttonElement = template.content.querySelector('button') as HTMLButtonElement | null;
    buttonElement?.addEventListener('click', this.highlightCodeBlock.bind(this));

    const first = template.content.firstElementChild;
    if (first) {
      this.replaceWith(first);
    }
  }

  connectedCallback(): void {
    console.log('connectedCallback');
    this.loadAndHighlightSourceCode();
  }

  private loadAndHighlightSourceCode(): void {
    this.loadSourceCodeAsync(this.getAttributes().file).then(() => {
      console.log(`Source code is loaded from ${this.getAttribute('file')}`);
      if ((Reveal as any).isReady()) {
        this.highlightCodeBlock();
      } else {
        (Reveal as any).on('ready', () => {
          this.highlightCodeBlock();
        });
      }
    });
  }

  private async loadSourceCodeAsync(file: string): Promise<void> {
    try {
      const absoluteFilename = `./public/src-to-present/${file}`;
      console.log(`Reading ${absoluteFilename}`);
      const response = await fetch(`${absoluteFilename}?raw`);
      if (!response.ok) {
        throw new Error(`Failed to fetch file: ${response.statusText}`);
      }
      if (this.#codeElement) {
        this.#codeElement.textContent = await this.readRequiredLines(response);
      }
      console.log(`file loaded: ${file}`);
    } catch (error: unknown) {
      const message = error instanceof Error ? error.message : String(error);
      if (this.#codeElement) {
        this.#codeElement.textContent = `Error loading file: ${message}`;
      }
    }
  }

  private async readRequiredLines(response: Response): Promise<string> {
    const { fromLine, toLine } = this.getAttributes();
    console.log(this.getAttributes());

    const text = await response.text();
    const lines = text.split('\n');
    let toLineIndex = lines.length - 1;
    if (toLine) {
      toLineIndex = toLine - 1;
    }
    let content = lines[fromLine - 1] ?? '';
    for (let i = fromLine; i <= toLineIndex; i++) {
      content += `\n${lines[i] ?? ''}`;
    }

    return content;
  }

  disconnectedCallback(): void {
    console.log('disconnectedCallback');
  }

  private highlightCodeBlock(): void {
    console.log('highlightCodeBlock');
    const highlight = (Reveal as any).getPlugin('highlight');
    if (this.#codeElement && highlight?.highlightBlock) {
      highlight.highlightBlock(this.#codeElement);
    }
  }

  private getAttributes(): Attributes {
    return {
      file: this.getAttribute('file') ?? '',
      fromLine: parseInt(this.getAttribute('from-line') ?? '') || 1,
      toLine: parseInt(this.getAttribute('to-line') ?? '') || null,
      lines: this.getAttribute('lines'),
      highlightLines: this.getAttribute('highlight-lines'),
      title: this.getAttribute('title'),
    };
  }
}

customElements.define('source-code-slide', SourceCodeSlide);