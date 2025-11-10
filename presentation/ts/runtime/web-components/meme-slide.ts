type Attributes = {
  imageUrl: string;
  creditsUrl: string;
  title: string;
};

class MemeSlide extends HTMLElement {

  constructor() {
    super();
    const template = document.createElement('template');
    const { imageUrl, creditsUrl, title } = this.getAttributes();
    template.innerHTML = `<section data-background-image="${imageUrl}">
        <h2 class="more-contrast">${title}</h2>
      
        <aside class="notes">
          ${creditsUrl}
        </aside>
      </section>`;

    const first = template.content.firstElementChild;
    if (first) {
      this.replaceWith(first);
    }
  }

  connectedCallback(): void {
    console.log('connectedCallback');
  }

  disconnectedCallback(): void {
    console.log('disconnectedCallback');
  }

  private getAttributes(): Attributes {
    return {
      imageUrl: this.getAttribute('img') ?? '',
      creditsUrl: this.getAttribute('credits') ?? '',
      title: this.getAttribute('title') ?? ''
    };
  }
}

customElements.define('meme-slide', MemeSlide);