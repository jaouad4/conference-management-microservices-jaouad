package ma.jaouad.conferenceservice;

import ma.jaouad.conferenceservice.config.FeignClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(defaultConfiguration = FeignClientConfig.class)
public class ConferenceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConferenceServiceApplication.class, args);
    }

}
