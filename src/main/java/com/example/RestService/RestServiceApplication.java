package com.example.RestService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;




@SpringBootApplication(scanBasePackages = "com.example.RestService")
@EnableCaching
@EnableScheduling
public class RestServiceApplication {

	@Bean
public CommandLineRunner checkControllers(org.springframework.context.ApplicationContext ctx) {
    return args -> {
        System.out.println("Registered controllers:");
        String[] controllerNames = ctx.getBeanNamesForAnnotation(RestController.class);
        for (String name : controllerNames) {
            System.out.println("- " + name);
        }
    };
}

@Bean
public CommandLineRunner checkMappings(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping mappings) {
    return args -> {
        System.out.println("Mapped endpoints:");
        mappings.getHandlerMethods().forEach((key, value) -> {
            System.out.println(key + " → " + value);
        });
    };
}

	public static void main(String[] args) {
		SpringApplication.run(RestServiceApplication.class, args);
	}

}
