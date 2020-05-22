package ua.darksoul.testprojects.ownchat;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Sentry.capture("Application OwnChat was started at " + LocalDateTime.now());
        SpringApplication.run(Application.class, args);
    }
}
