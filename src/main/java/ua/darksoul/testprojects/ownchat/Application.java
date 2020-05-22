package ua.darksoul.testprojects.ownchat;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Sentry.capture("Application OwnChat was started.");
        SpringApplication.run(Application.class, args);
    }
}
