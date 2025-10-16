import { defineConfig } from 'vite';
import { copyRequiredSourceFiles } from "./js/build-time/copy-required-source-files";

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
    }
  ]
});