package com.pratima.makemyenotes;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MakemyenotesApplication {

    //test
    @Bean
    public OpenAPI inventoryOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Pratima BCA Final Year: Make My e Notes API")
                        .description("API documentation for Make My e Notes")
                        .version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

	public static void main(String[] args) {
		SpringApplication.run(MakemyenotesApplication.class, args);
	}

}
