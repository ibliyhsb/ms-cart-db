package cl.duoc.ms_cart_db.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cart microservice - Database(DB) Documentation")
                        .version("1.0")
                        .description("This microservice is responsible for retrieving and also inserting information from the database and allows the corresponding queries to be executed"));
    }
}
