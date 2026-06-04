package dev.somlyaip.blog.youdontknowhibernate.partial_fetch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@SpringBootApplication
public class PartialFetchApplication  {

    public static void main(String[] args) {
        SpringApplication.run(PartialFetchApplication.class, args);
    }

}
