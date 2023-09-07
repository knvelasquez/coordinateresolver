package config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"adapter.presentation", "adapter.infrastructure", "application", "config"})
@EntityScan(basePackages = {""})
@SpringBootApplication
public class CoordinateResolverMain {

    public static void main(String[] args) {
        SpringApplication.run(CoordinateResolverMain.class, args);
    }

}
