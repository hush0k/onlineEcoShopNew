package kz.kbtu.onlineecoshopnew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OnlineEcoShopNewApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineEcoShopNewApplication.class, args);
    }

}
