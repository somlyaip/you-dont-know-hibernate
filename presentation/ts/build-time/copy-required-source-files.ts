import fs from 'fs';
import path from 'path';

// Small pure helper to extract file paths from HTML content (for unit testing)
export function extractSourceCodeFilePaths(html: string): string[] {
  const results: string[] = [];
  const regExp = new RegExp(/<source-code\b[\s\S]*?\bfile="([^"]*)"/igs);
  let match: RegExpExecArray | null;
  while ((match = regExp.exec(html)) !== null) {
    results.push(match[1].trim());
  }
  return results;
}

export function copyRequiredSourceFiles(
  pathToJavaProjectRoot: string,
  pathToPublicSrcToPresent: string,
  pathToHtmlFile: string
): void {
  console.debug('Making required sources available for RevealJS...');

  // Collect HTML files to scan: the provided HTML file plus all .html under slides/ recursively
  const filesToScan: string[] = [];
  // TODO: file in 'pathToHtmlFile' doesn't contain any source-code,
  //  so remove this logic and use another parameter
  const resolvedHtmlPath = path.resolve(pathToHtmlFile);
  if (fs.existsSync(resolvedHtmlPath) && fs.statSync(resolvedHtmlPath).isFile()) {
    filesToScan.push(resolvedHtmlPath);
  }

  const slidesDir = path.resolve(path.dirname(resolvedHtmlPath), 'slides');
  if (fs.existsSync(slidesDir) && fs.statSync(slidesDir).isDirectory()) {
    const walk = (dir: string) => {
      for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
        const full = path.join(dir, entry.name);
        if (entry.isDirectory()) {
          walk(full);
        } else if (entry.isFile() && full.toLowerCase().endsWith('.html')) {
          filesToScan.push(full);
        }
      }
    };
    walk(slidesDir);
  }

  for (const filePath of filesToScan) {
    const content: string = fs.readFileSync(filePath, 'utf-8');
    const sourceCodeFilePaths = extractSourceCodeFilePaths(content);
    sourceCodeFilePaths.forEach((relativeSourceFilename) => {
      const fromPath = path.resolve(pathToJavaProjectRoot, relativeSourceFilename);
      const toPath = path.resolve(pathToPublicSrcToPresent, relativeSourceFilename);
      fs.mkdirSync(path.dirname(toPath), { recursive: true });
      console.debug(`  - Copying ${fromPath} to ${toPath}`);
      fs.copyFileSync(fromPath, toPath);
    });
  }

  console.debug('All source files copied');
}