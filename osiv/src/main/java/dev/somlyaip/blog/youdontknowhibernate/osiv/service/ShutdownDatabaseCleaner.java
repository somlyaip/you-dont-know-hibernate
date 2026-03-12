package dev.somlyaip.blog.youdontknowhibernate.osiv.service;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShutdownDatabaseCleaner implements ApplicationListener<ContextClosedEvent> {

    private final DataSource dataSource;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("Shutting down application...");
        log.info("Deleting database tables starting...");
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("""
                                DROP TABLE IF EXISTS issue;
                                DROP TABLE IF EXISTS estimation;
                                DROP TABLE IF EXISTS feature;
                                DROP TABLE IF EXISTS project;
                                DROP TABLE IF EXISTS project_architect;
                                DROP SEQUENCE IF EXISTS project_architect_seq;
                                DROP SEQUENCE IF EXISTS project_seq;
                                DROP SEQUENCE IF EXISTS issue_seq;
                                DROP SEQUENCE IF EXISTS feature_seq;
                                DROP SEQUENCE IF EXISTS estimation_seq;
                                DROP TABLE DATABASECHANGELOG;
                                DROP TABLE DATABASECHANGELOGLOCK;
                """
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("All tables dropped.");
    }

}
