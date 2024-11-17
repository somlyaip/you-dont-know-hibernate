package dev.somlyaip.blog.youdontknowhibernate.common.testharness;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TestHarnessAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TestDataPopulator testDataPopulator(DataSource dataSource) {
        return new TestDataPopulator(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public TestDatasourceUrlPrinterCmdRunner testDatasourceUrlPrinterCmdRunner(DataSource dataSource) {
        return new TestDatasourceUrlPrinterCmdRunner(dataSource);
    }

    @Bean
    @Profile("test")
    public DatasourceProxyBeanPostProcessor datasourceProxyBeanPostProcessor() {
        return new DatasourceProxyBeanPostProcessor();
    }
}
