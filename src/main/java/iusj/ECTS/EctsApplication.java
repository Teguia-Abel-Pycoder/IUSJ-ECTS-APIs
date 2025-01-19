package iusj.ECTS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "iusj.ECTS")
public class EctsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EctsApplication.class, args);
	}

}
