package indimetra.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para habilitar el bean {@link Validator} de validaciones con
 * Bean Validation (Jakarta).
 * <p>
 * Esto permite validar DTOs de forma programática usando
 * `validator.validate(...)`.
 */
@Configuration
public class ValidationConfig {

    /**
     * Crea un bean de {@link Validator} a partir de la factoría por defecto.
     *
     * @return una instancia de Validator lista para inyección
     */
    @Bean
    public Validator validator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }
}
