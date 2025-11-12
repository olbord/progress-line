package ee.taltech.veebirakenduseprojekt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.*;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class VeebirakenduseProjektApplication {

    public static void main(String[] args) {
        SpringApplication.run(VeebirakenduseProjektApplication.class, args);
    }

}
