package dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class LazyMultipleToOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(LazyMultipleToOneApplication.class, args);
    }
}
