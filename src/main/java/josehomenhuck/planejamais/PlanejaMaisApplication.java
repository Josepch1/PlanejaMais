package josehomenhuck.planejamais;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlanejaMaisApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlanejaMaisApplication.class, args);
    }

}
