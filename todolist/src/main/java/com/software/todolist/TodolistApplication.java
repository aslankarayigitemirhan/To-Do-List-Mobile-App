package com.software.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "com.software")
@ComponentScan(basePackages = {"com.software", "com.software.config"}) // SecurityConfig sınıfının bulunduğu paket de dahil
@EnableJpaRepositories(basePackages = "com.software")
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

}
@Configuration
class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://10.0.2.2:8080", "http://localhost:8080","http://localhost:3000") // Emülatör ve yerel test için
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS eklendi
				.allowedHeaders("Authorization", "Content-Type") // Gerekli başlıklar
				.allowCredentials(false) // Kimlik bilgileri gerekmiyorsa false
				.maxAge(3600); // Preflight cache süresi
	}
}