package ru.khanin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(
	scanBasePackages = {"ru.lib.khanin", "ru.khanin"}
)
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

	}
}
