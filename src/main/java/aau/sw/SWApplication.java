package aau.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class SWApplication {

    public static void main(String[] args) {
        SpringApplication.run(SWApplication.class, args);
    }

}
