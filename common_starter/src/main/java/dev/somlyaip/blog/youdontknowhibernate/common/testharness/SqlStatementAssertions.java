package dev.somlyaip.blog.youdontknowhibernate.common.testharness;

import io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator;

public class SqlStatementAssertions {

    private SqlStatementAssertions() {
    }

    public static SqlStatementAssertions assertThatSqlStatements(Runnable task) {
        // Credits: https://vladmihalcea.com/how-to-detect-the-n-plus-one-query-problem-during-testing/
        SQLStatementCountValidator.reset();
        task.run();
        return new SqlStatementAssertions();
    }

    @SuppressWarnings("UnusedReturnValue")
    public SqlStatementAssertions hasSelectCount(int expectedCount) {
        SQLStatementCountValidator.assertSelectCount(expectedCount);
        return this;
    }

    @SuppressWarnings("unused")
    public SqlStatementAssertions hasInsertCount(int expectedCount) {
        SQLStatementCountValidator.assertInsertCount(expectedCount);
        return this;
    }

    @SuppressWarnings("unused")
    public SqlStatementAssertions hasUpdateCount(int expectedCount) {
        SQLStatementCountValidator.assertUpdateCount(expectedCount);
        return this;
    }

    @SuppressWarnings("unused")
    public SqlStatementAssertions hasDeleteCount(int expectedCount) {
        SQLStatementCountValidator.assertDeleteCount(expectedCount);
        return this;
    }

}
