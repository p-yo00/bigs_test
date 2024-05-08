package api.spring.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "api.spring")
@EnableJpaRepositories(basePackages = "api.spring")
@SpringBootApplication(scanBasePackages = "api.spring")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
