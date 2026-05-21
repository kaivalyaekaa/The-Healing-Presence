package in.thehealingpresence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ConfigurationPropertiesScan("in.thehealingpresence.config.properties")
@EnableAsync
@EnableCaching
public class HealingPresenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealingPresenceApplication.class, args);
    }
}
