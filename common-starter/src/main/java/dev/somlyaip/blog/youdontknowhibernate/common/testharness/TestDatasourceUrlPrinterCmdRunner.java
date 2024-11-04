package dev.somlyaip.blog.youdontknowhibernate.common.testharness;

import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestDatasourceUrlPrinterCmdRunner implements CommandLineRunner {

    private final DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        log.info("Using datasource URL: {}", dataSource.getConnection().getMetaData().getURL());
    }
}
