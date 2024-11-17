package dev.somlyaip.blog.youdontknowhibernate.common.testharness;

import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class TestDataPopulator {

    private final DataSource dataSource;

    public void executeScriptsFromResources(String... pathToScripts) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        for (String path : pathToScripts) {
            populator.addScript(new ClassPathResource(path));
        }
        populator.execute(this.dataSource);
    }

}
