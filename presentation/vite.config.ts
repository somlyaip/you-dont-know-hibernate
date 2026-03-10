import { defineConfig } from 'vite';
import { copyRequiredSourceFiles } from "./ts/build-time/copy-required-source-files";
import { loadSlideFragments } from "./ts/build-time/load-slide-fragments";
import path from 'node:path';
import fs from 'node:fs';
import { fileURLToPath } from 'node:url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Configurable title replacements for filesystem-based title creation
// Adjust or extend this map to transform specific text chunks found in filenames into special characters.
// Example: " n_" -> "?" lets you write filenames like "Why so slow n_.md" that render as "Why so slow?"
const titleReplacements = {
  " n_": "?",
};

export default defineConfig({
  test: {
    environment: 'jsdom',
  },
  plugins: [
    {
      name: 'copy-required-source-files',
      buildStart() {
        const slidesDir = path.resolve(__dirname, 'slides');
        if (fs.existsSync(slidesDir)) {
          const walk = (dir: string) => {
            fs.readdirSync(dir).forEach((file) => {
              const fullPath = path.join(dir, file);
              if (fs.statSync(fullPath).isDirectory()) {
                walk(fullPath);
              } else if (file.endsWith('.html')) {
                copyRequiredSourceFiles(
                  '../',
                  './public/src-to-present', // TODO: extract it into config
                  fullPath
                );
              }
            });
          };
          walk(slidesDir);
        }
      },
      handleHotUpdate({ file, server }) {
        if (file.includes(`${path.sep}slides${path.sep}`)) {
          console.log(`Source file changed: ${file}. Re-copying files.`);
          copyRequiredSourceFiles(
            '../',
            './public/src-to-present', // TODO: extract it into config
            "index.html"
          );
          server.ws.send({ type: 'full-reload' });
        }
      }
    },
    {
      name: 'merge-slides-into-index',
      enforce: 'pre',
      transformIndexHtml(html) {
        const merged = loadSlideFragments(__dirname, { titleReplacements });
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
  ],
  resolve: {
    alias: [
      {
       // Any import of 'highlight.js' (e.g. from reveal.js) will use our mock
       find: /^highlight\.js$/,
       replacement: path.resolve(__dirname, 'ts/runtime/highlightjs-replacement.ts'),
      }
    ]
  }

});