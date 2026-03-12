package dev.somlyaip.blog.youdontknowhibernate.osiv;

import dev.somlyaip.blog.youdontknowhibernate.osiv.service.InitDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@SpringBootApplication
@RequiredArgsConstructor
public class OsivApplication implements CommandLineRunner {

    private final InitDbService initDbService;

    public static void main(String[] args) {
        SpringApplication.run(OsivApplication.class, args);
    }

    @Override
    public void run(String... args) {
        initDbService.initDatabase();
    }

}
