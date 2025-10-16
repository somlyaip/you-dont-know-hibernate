import fs from 'fs';
import path from 'path'

export function copyRequiredSourceFiles(pathToJavaProjectRoot, pathToPublicSrcToPresent, pathToHtmlFile) {
  console.log('Making required sources available for RevealJS...')
  const content = fs.readFileSync(pathToHtmlFile, 'utf-8');
  const regExp = new RegExp(/<source-code-slide .*file="([^"]*)"/, 'ig');
  let match;
  while (match = regExp.exec(content)) {
    const [, relativeSourceFilename] = match;
    const fromPath = path.resolve(pathToJavaProjectRoot, relativeSourceFilename);
    const toPath = path.resolve(pathToPublicSrcToPresent, relativeSourceFilename);
    fs.mkdirSync(path.dirname(toPath), { recursive: true });
    console.log(`  - Copying ${fromPath} to ${toPath}`);
    fs.copyFileSync(fromPath, toPath);
  }
}