import { describe, expect, it } from 'vitest';

import { extractSourceCodeFilePaths } from './copy-required-source-files';

describe('SOURCE_CODE_FILE_ATTR_PATTERN', () => {
  it('matches single-line <source-code> with file and other attributes', () => {
    const html = [
      '<section>',
      '  <h3>Example</h3>',
      '  <source-code file="eager_n_plus_1/src/test/java/dev/somlyaip/blog/youdontknowhibernate/eager_n_plus_1/EagerNPlus1SelectWithSqlStatementCountAssertionTest.java" highlight-lines="56-63"></source-code>',
      '</section>',
    ].join('\n');

    const files = extractSourceCodeFilePaths(html);
    expect(files).toEqual([
      'eager_n_plus_1/src/test/java/dev/somlyaip/blog/youdontknowhibernate/eager_n_plus_1/EagerNPlus1SelectWithSqlStatementCountAssertionTest.java',
    ]);
  });

  it('matches multi-line <source-code> where attributes span lines', () => {
    const html = `<section>
      <h3>Example</h3>
      <source-code file="eager_n_plus_1/src/test/java/dev/somlyaip/blog/youdontknowhibernate/eager_n_plus_1/EagerNPlus1SelectWithSqlStatementCountAssertionTest.java"
        highlight-lines="56-63"
        >
      </source-code>
    </section>`;

    const files = extractSourceCodeFilePaths(html);
    expect(files).toEqual([
      'eager_n_plus_1/src/test/java/dev/somlyaip/blog/youdontknowhibernate/eager_n_plus_1/EagerNPlus1SelectWithSqlStatementCountAssertionTest.java',
    ]);
  });

  it('matches multi-line <source-code> where file attribute is in a new line', () => {
    const html = `<section>
      <h3>Example</h3>
      <source-code 
        file="eager_n_plus_1/src/test/java/dev/somlyaip/blog/youdontknowhibernate/eager_n_plus_1/EagerNPlus1SelectWithSqlStatementCountAssertionTest.java"
        highlight-lines="56-63"
        >
      </source-code>
    </section>`;

    const files = extractSourceCodeFilePaths(html);
    expect(files).toEqual([
      'eager_n_plus_1/src/test/java/dev/somlyaip/blog/youdontknowhibernate/eager_n_plus_1/EagerNPlus1SelectWithSqlStatementCountAssertionTest.java',
    ]);
  });

  it('matches when other attributes appear before file attribute', () => {
    const html = '<source-code language="java" data-x="1" file="path/to/File.java"></source-code>';
    const files = extractSourceCodeFilePaths(html);
    expect(files).toEqual(['path/to/File.java']);
  });

  it('does not match tags without file attribute', () => {
    const html = '<source-code language="sql"></source-code>';
    const files = extractSourceCodeFilePaths(html);
    expect(files).toEqual([]);
  });

  it('extracts multiple occurrences across the document', () => {
    const html = [
      '<source-code file="a/A.java"></source-code>',
      '<div>middle</div>',
      '<source-code file="b/B.java" highlight-lines="1-2"></source-code>',
    ].join('\n');

    const files = extractSourceCodeFilePaths(html);
    expect(files).toEqual(['a/A.java', 'b/B.java']);
  });
});
