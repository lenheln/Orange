package elena.kozlova.orange;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OrangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrangeApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}


	//TODO тесты написать
	//TODO oracle подключить вместо постгреса
	//TODO изменить url внешнего rest
}
