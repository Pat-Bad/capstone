package it.epicode.capstone.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permetti il CORS su tutti gli endpoint che iniziano con "/api/"
        registry.addMapping("/api/**") // Questo include tutti gli endpoint che iniziano con /api/
                .allowedOrigins("http://localhost:5173") // Sostituisci con il dominio del tuo frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // I metodi HTTP consentiti
                .allowedHeaders("*") // Consenti tutti gli header
                .allowCredentials(true); // Se necessario
    }
}
