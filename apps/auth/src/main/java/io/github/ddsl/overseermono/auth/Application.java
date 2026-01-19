package io.github.ddsl.overseermono.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "io.github.ddsl.overseermono.commonlib.repositories")
@EntityScan("io.github.ddsl.overseermono.commonlib.entities")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
