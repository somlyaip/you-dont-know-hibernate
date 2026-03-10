- I’ve implemented an assertion tool facade
  - Which can assert executed SQL statements count
  - Based on Vlad Michalcea’s work
    - DataSource bean is overridden by a special DataSourceProxy
      - Which decorates it
        - counts SQL statements
  - It should be used during development to prevent N+1 query problems
  
Notes:
dev.somlyaip.blog.youdontknowhibernate.common.testharness.SqlStatementAssertions#hasSelectCount

Credits:
- https://vladmihalcea.com/how-to-detect-the-n-plus-one-query-problem-during-testing/
- https://github.com/ttddyy/datasource-proxy-examples/blob/master/springboot-autoconfig-example/src/main/java/net/ttddyy/dsproxy/example/DatasourceProxyBeanPostProcessor.java#L20