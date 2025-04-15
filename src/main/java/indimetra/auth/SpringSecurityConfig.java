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
                // SWAGGER - permitir acceso sin autenticación
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
    
                // AUTHORIZATION
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()

                // CORTOMETRAJES
                        // Rutas públicas
                        .requestMatchers(HttpMethod.GET,
                                "/cortometraje",
                                "/cortometraje/buscar/mis-cortometrajes",
                                "/cortometraje/buscar/{title}",
                                "/cortometraje/buscar/categoria/{categoryName}",
                                "/cortometraje/buscar/latest",
                                "/cortometraje/buscar/rating-minimo/{valor}",
                                "/cortometraje/buscar/top5-mejor-valorados",
                                "/cortometraje/buscar/duracion-maxima/{minutos}",
                                "/cortometraje/paginated",
                                "/cortometraje/buscar/autor/{username}",
                                "/cortometraje/buscar/idioma/{language}",
                                "/cortometraje/{id}")
                        .permitAll()
                        // Rutas ROLE_USER
                        .requestMatchers(HttpMethod.GET, "/cortometraje/buscar/mis-cortometrajes").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/cortometraje").hasAuthority("ROLE_USER")
                        // Rutas ROLE_ADMIN / ROLE_USER(owner)
                        .requestMatchers(HttpMethod.DELETE, "/cortometraje/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/cortometraje/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

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
                        // Rutas públicas
                        .requestMatchers(HttpMethod.GET,
                                "/review",
                                "/review/buscar/por-cortometraje/{cortometrajeId}",
                                "/review/buscar/por-usuario/{username}",
                                "/review/{id}")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/review").hasAuthority("ROLE_USER")

                        // ROLE_USER(owner) / ROLE_ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/review/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        
                        //USER_OWNER
                        .requestMatchers(HttpMethod.GET, "/review/mis-reviews").hasAnyAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.PUT, "/review/{id}").hasAuthority("ROLE_USER")

                // FAVORITE
                        // Rutas USER
                        .requestMatchers(HttpMethod.POST, "/favorite").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/favorite/mis-favoritos").hasAuthority("ROLE_USER")

                        // ROLE_USER(owner) / ROLE_ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/favorite/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // ROLE_ADMIN
                        // .requestMatchers(HttpMethod.GET, "/favorite/all").hasAuthority("ROLE_ADMIN")
                
                // ROLE
                        // ROLE_ADMIN
                        .requestMatchers("/role/**").hasAuthority("ROLE_ADMIN")

                // USER
                        // ROLE_ADMIN
                        .requestMatchers(HttpMethod.GET, 
                                "/user",
                                "/user/paginated",
                                "/user/paginated/active",
                                "/user/{id}",
                                "/user/stats",
                                "/user/buscar/by-role/{role}",
                                "/user/buscar/by-username/{username}"
                        ).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, 
                                "/user/toggle-role/{id}",
                                "/user/deactivate/{id}",
                                "/user/reactivate/{id}",
                                "/user/soft-delete/{id}"
                        ).hasAuthority("ROLE_ADMIN")

                        // ROLE_USER(owner)
                        .requestMatchers(HttpMethod.PUT, 
                                "/user/me",
                                "user/me/delete",
                                "/user/me/change-password"
                        ).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        
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
