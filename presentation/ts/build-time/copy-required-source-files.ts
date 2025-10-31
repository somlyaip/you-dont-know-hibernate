import fs from 'fs';
import path from 'path';

export function copyRequiredSourceFiles(
  pathToJavaProjectRoot: string,
  pathToPublicSrcToPresent: string,
  pathToHtmlFile: string
): void {
  console.log('Making required sources available for RevealJS...');
  const content: string = fs.readFileSync(pathToHtmlFile, 'utf-8');
  const regExp: RegExp = /<source-code-slide\b[\s\S]*?\bfile="([^"]*)"/igs;
  let match: RegExpExecArray | null;
  while ((match = regExp.exec(content)) !== null) {
    const [, relativeSourceFilename] = match;
    const fromPath = path.resolve(pathToJavaProjectRoot, relativeSourceFilename);
    const toPath = path.resolve(pathToPublicSrcToPresent, relativeSourceFilename);
    fs.mkdirSync(path.dirname(toPath), { recursive: true });
    console.log(`  - Copying ${fromPath} to ${toPath}`);
    fs.copyFileSync(fromPath, toPath);
  }
}