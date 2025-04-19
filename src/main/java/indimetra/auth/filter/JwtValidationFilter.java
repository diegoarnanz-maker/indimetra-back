package indimetra.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import indimetra.auth.SimpleGrantedAuthorityJsonCreator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.*;

import static indimetra.auth.TokenJwtConfig.*;

/**
 * Filtro que valida el token JWT en cada petición HTTP protegida.
 * <p>
 * Este filtro:
 * <ul>
 * <li>Lee el token JWT de la cabecera Authorization</li>
 * <li>Valida su firma usando {@code SECRET_KEY}</li>
 * <li>Extrae el username y roles del token</li>
 * <li>Establece el contexto de autenticación para que Spring Security reconozca
 * al usuario</li>
 * </ul>
 * No se ejecuta en la ruta de login porque el login genera el token.
 */
public class JwtValidationFilter extends BasicAuthenticationFilter {

    /**
     * Constructor que recibe el AuthenticationManager necesario para el filtro
     * base.
     *
     * @param authenticationManager el AuthenticationManager global
     */
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * Método principal que intercepta todas las peticiones HTTP para validar el
     * JWT.
     *
     * @param request  petición HTTP
     * @param response respuesta HTTP
     * @param chain    cadena de filtros
     * @throws IOException      en caso de error de lectura/escritura
     * @throws ServletException en caso de error en el servlet
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(HEADER_AUTHORIZATION);

        // Si no hay token o no empieza por el prefijo esperado, continúa sin autenticar
        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "");

        try {
            // Validamos y parseamos el token
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject(); // extraemos el subject

            String authoritiesClaim = (String) claims.get("authorities");

            // Convertimos el JSON de roles a objetos GrantedAuthority
            Collection<? extends GrantedAuthority> roles = Arrays.asList(
                    new ObjectMapper()
                            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                            .readValue(authoritiesClaim.getBytes(), SimpleGrantedAuthority[].class));

            // Creamos el token de autenticación e inyectamos en el contexto de seguridad
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                    null, roles);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);

        } catch (JwtException e) {
            // Si el token es inválido o ha expirado, devolvemos respuesta 401
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token es inválido o ha expirado.");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(CONTENT_TYPE);
        }
    }
}
