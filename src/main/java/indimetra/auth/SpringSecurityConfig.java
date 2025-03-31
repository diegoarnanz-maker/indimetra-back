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

import indimetra.auth.filter.JwtAuthenticationFilter;
import indimetra.auth.filter.JwtValidationFilter;

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

        AuthenticationManager authManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(authorize -> authorize
                        // AUTHORIZATION
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()

                        // CORTOMETRAJES
                        // Rutas públicas
                        .requestMatchers(HttpMethod.GET,
                                "/cortometraje",
                                "/cortometraje/paginated",
                                "/cortometraje/{id}")
                        .permitAll()
                        // Rutas ROLE_USER
                        .requestMatchers(HttpMethod.POST, "/cortometraje").hasAuthority("ROLE_USER")

                        // CATEGORY
                        // Rutas públicas
                        .requestMatchers(HttpMethod.GET,
                                "/category",
                                "/category/{id}")
                        .permitAll()

                        // Rutas ROLE_ADMIN
                        .requestMatchers(HttpMethod.POST, "/category").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/category/{id}").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/category/{id}").hasAuthority("ROLE_ADMIN")

                        // REVIEW
                        .requestMatchers(HttpMethod.GET,
                                "/review",
                                "/review/{id}")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/review").hasAuthority("ROLE_USER")

                        // ROLE_USER(owner) / ROLE_ADMIN
                        .requestMatchers(HttpMethod.PUT, "/review/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/review/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // FAVORITE
                        // Rutas públicas
                        .requestMatchers(HttpMethod.POST, "/favorite").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/favorite").hasAuthority("ROLE_USER")

                        // ROLE_USER(owner) / ROLE_ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/favorite/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // ROLE_ADMIN
                        // .requestMatchers(HttpMethod.GET, "/favorite/all").hasAuthority("ROLE_ADMIN")

                        // USER
                        // ROLE_ADMIN

                        // ROLE_ADMIN / ROLE_USER(owner)
                        
                        // OTRAS
                        .anyRequest().authenticated())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(new JwtAuthenticationFilter(authManager))
                .addFilter(new JwtValidationFilter(authManager))
                .build();
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
