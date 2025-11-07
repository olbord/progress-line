package ee.taltech.veebirakenduseprojekt.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Project ProgressLine",
                version = "v1.0.0",
                description = "This is the documentation for Spring Boot Application ProgressLine."
        )
)
public class OpenApiConfig {

}
