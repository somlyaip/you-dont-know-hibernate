package dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1;

import dev.somlyaip.blog.youdontknowhibernate.common.testharness.TestDataPopulator;
import dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EagerNPlus1Application {

    public static void main(String[] args) {
        SpringApplication.run(EagerNPlus1Application.class, args);
    }
}