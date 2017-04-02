package ba.steleks;

import ba.steleks.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SteleksServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SteleksServiceApplication.class, args);
	}
}
