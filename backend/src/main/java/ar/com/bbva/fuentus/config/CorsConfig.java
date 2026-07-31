package ar.com.bbva.fuentus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS para el backend de Fuentus.
 * Permite solicitudes desde el frontend y otros orígenes especificados.
 */
@Configuration
public class CorsConfig {

    @Value("${cors.allowed.origins:http://localhost:3000,http://localhost:5173}")
    private String[] allowedOrigins;

    /**
     * Configura CORS para permitir solicitudes cross-origin.
     * 
     * @return WebMvcConfigurer con la configuración de CORS
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // Permitir orígenes desde configuración
                        .allowedOrigins(allowedOrigins)
                        // Permitir todos los métodos HTTP comunes
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        // Permitir todos los headers
                        .allowedHeaders("*")
                        // Permitir credenciales (cookies, authorization headers)
                        .allowCredentials(true)
                        // Tiempo máximo de caché de la respuesta preflight
                        .maxAge(3600);
            }
        };
    }
}
