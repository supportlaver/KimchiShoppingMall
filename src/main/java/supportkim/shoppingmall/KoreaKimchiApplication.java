package supportkim.shoppingmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KoreaKimchiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KoreaKimchiApplication.class, args);
	}

}
