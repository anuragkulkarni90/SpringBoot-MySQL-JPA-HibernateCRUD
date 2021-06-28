package demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import static org.springframework.boot.SpringApplication.run;

@EntityScan(basePackages = "demo.entity")
@SpringBootApplication
public class CrudApplication {

	public static void main(String[] args) {
		run(CrudApplication.class, args);
	}
}