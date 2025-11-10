import Reveal from 'reveal.js';

type Attributes = {
  file: string;
  fromLine: number;
  toLine: number | null;
  lines: string | null;
  highlightLines: string | null;
  language: string | null;
  trimColumnsCount: number;
};

class SourceCode extends HTMLElement {
  #codeElement: HTMLElement | null = null;

  constructor() {
    super();
    const template = document.createElement('template');
    const { fromLine, highlightLines, language, file } = this.getAttributes();
    console.log(this.innerHTML);
    template.innerHTML = `<div>
        <pre ${language ? `class="language-${language}"` : ''}>
         <code 
            data-trim
            ${fromLine ? `data-ln-start-from="${fromLine}"` : ''}
            ${highlightLines ? `data-line-numbers="${(this.getRelativeHighlightLines(highlightLines, fromLine))}"` : 'data-line-numbers'}
            >${file ? 'Loading...' : this.innerHTML}</code>
        </pre>
            
        <div class="code-side-bottom-toolbar">
          ${file ? `<span>${file}</span>` : ''}
          <button class="force-highlighting-button">&#x21bb;</button>
        </div>
      </div>`;
    // TODO: append design to the ugly button above

    this.#codeElement = template.content.querySelector('code') as HTMLElement | null;
    const buttonElement = template.content.querySelector('button') as HTMLButtonElement | null;
    buttonElement?.addEventListener('click', this.highlightCodeBlock.bind(this));

    const first = template.content.firstElementChild;
    if (first) {
      this.replaceWith(first);
    }
  }

  /**
  * RevealJS uses relative line numbers from 'fromLine'
  * @param highlightLines
  * @param fromLine
  * @private
  */
  private getRelativeHighlightLines(highlightLines: string, fromLine: number): string {
    if (!highlightLines) {
      return '';
    }
    const offset = fromLine - 1;
    return highlightLines.split('|')
      .map(part => {
        if (part.includes('-')) {
          const [start, end] = part.split('-').map(num => parseInt(num, 10));
          return `${start - offset}-${end - offset}`;
        }
        return (parseInt(part, 10) - offset).toString();
      })
      .join('|');
  }

  connectedCallback(): void {
    console.log('connectedCallback');
    if (this.getAttributes().file) {
      this.loadAndHighlightSourceCode();
    } else {
      this.highlightSourceCodeTimingSafe()
    }
  }

  private loadAndHighlightSourceCode(): void {
    this.loadSourceCodeAsync(this.getAttributes().file).then(() => {
      console.log(`Source code is loaded from ${this.getAttribute('file')}`);
      this.highlightSourceCodeTimingSafe();
    });
  }

  private highlightSourceCodeTimingSafe(){
      if ((Reveal as any).isReady()) {
        this.highlightCodeBlock();
      } else {
        (Reveal as any).on('ready', () => {
          this.highlightCodeBlock();
        });
      }
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
    const { fromLine, toLine, trimColumnsCount } = this.getAttributes();
    console.log(this.getAttributes());

    const text = await response.text();
    const lines = text.split('\n');
    let toLineIndex = lines.length - 1;
    if (toLine) {
      toLineIndex = toLine - 1;
    }
    let content = this.getLineAndTrimIfNeeded(lines, fromLine - 1, trimColumnsCount);
    for (let i = fromLine; i <= toLineIndex; i++) {
      content += `\n${this.getLineAndTrimIfNeeded(lines, i, trimColumnsCount)}`;
    }

    return content;
  }

  private getLineAndTrimIfNeeded(lines: string[], index: number, trimColumnsCount: number): string {
    let line = lines[index] ?? '';
    if (trimColumnsCount) {
      line = line.substring(trimColumnsCount);
    }
    return line;
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

  // TODO: use a TS getter
  private getAttributes(): Attributes {
    return {
      file: this.getAttribute('file') ?? '',
      fromLine: parseInt(this.getAttribute('from-line') ?? '') || 1,
      toLine: parseInt(this.getAttribute('to-line') ?? '') || null,
      lines: this.getAttribute('lines'),
      highlightLines: this.getAttribute('highlight-lines'),
      language: this.getAttribute('language')
        ?? this.getExtensionOf(this.getAttribute('file')),
      trimColumnsCount: parseInt(this.getAttribute('trim-cols') ?? '0'),
    };
  }

  private getExtensionOf(file: string | null): string | null {
    if (!file) {
      return null
    }

    const lastDot = file.lastIndexOf('.');
    return file.slice(lastDot + 1);
  }
}

customElements.define('source-code', SourceCode);