import { defineConfig } from 'vite';
import { copyRequiredSourceFiles } from "./ts/build-time/copy-required-source-files";
import { loadSlideFragments } from "./ts/build-time/load-slide-fragments";
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);


export default defineConfig({
  plugins: [
    {
      name: 'copy-required-source-files',
      buildStart() {
        copyRequiredSourceFiles(
          '../',
          './public/src-to-present', // TODO: extract it into config
          "index.html"
        );
      }
    },
    {
      name: 'merge-slides-into-index',
      enforce: 'pre',
      transformIndexHtml(html) {
        const merged = loadSlideFragments(__dirname);
        // Replace inner content of <div class="slides"> ... </div>
        const regex = /(\n?\s*<div class=\"slides\">)([\s\S]*?)(<\/div>)/m;
        if (regex.test(html)) {
          return html.replace(regex,
            (_whitespaces,divOpeningTag,_innerContent,divEndingTag) =>
              `${divOpeningTag}\n${merged}\n${divEndingTag}`);
        }
        return html;
      },
      handleHotUpdate(ctx) {
        // Reload when any slide fragment changes
        if (ctx.file.includes(`${path.sep}slides${path.sep}`)) {
          ctx.server.ws.send({ type: 'full-reload' });
        }
      }
    }
  ]
});