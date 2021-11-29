package br.com.sicredi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SicredApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SicredApiApplication.class, args);
	}

}
