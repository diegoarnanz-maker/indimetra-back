package indimetra.auth;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
// import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(authorize -> authorize
                        // AUTHORIZATION
                        .requestMatchers(HttpMethod.POST,"/auth/login", "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth/me").authenticated()
                        
                        // CORTOMETRAJES
                        // Rutas públicas
                        .requestMatchers(HttpMethod.GET,"/cortometraje").permitAll()
                        .requestMatchers(HttpMethod.GET,"/cortometraje/{id}").permitAll()
                        // Rutas ROLE_USER
                        .requestMatchers(HttpMethod.POST,"/cortometraje").hasAuthority("ROLE_USER")

                        // REVIEWS
                        // Rutas públicas

                        // Rutas ROLE_USER

                        // Rutas ROLE_ADMIN / ROLE_USER
                        

                        // FAVORITES
                        // Rutas ROLE_ADMIN / ROLE_USER
                        

                        // ROLE_USER(owner)
                        // .requestMatchers(HttpMethod.POST, "/api/favorites")
                        // .hasAuthority("ROLE_USER")

                        // USER
                        // ROLE_ADMIN

                        // ROLE_ADMIN / ROLE_USER(owner)

                        // OTRAS
                        .anyRequest().authenticated())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
