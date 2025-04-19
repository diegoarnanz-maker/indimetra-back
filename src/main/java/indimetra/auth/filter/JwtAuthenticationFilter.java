package indimetra.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import indimetra.modelo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.*;

import static indimetra.auth.TokenJwtConfig.*;

/**
 * Filtro de autenticación JWT que se ejecuta cuando un usuario intenta iniciar
 * sesión.
 * <p>
 * Este filtro:
 * <ul>
 * <li>Lee las credenciales del cuerpo de la petición (username y password)</li>
 * <li>Valida las credenciales utilizando el {@link AuthenticationManager}</li>
 * <li>Si son válidas, genera un JWT firmado y lo envía en la cabecera de la
 * respuesta y en el cuerpo</li>
 * </ul>
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    /**
     * Constructor que recibe el AuthenticationManager para delegar la
     * autenticación.
     *
     * @param authenticationManager componente encargado de autenticar las
     *                              credenciales del usuario
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/auth/login"); // URL donde se intercepta el login
    }

    /**
     * Método que intenta autenticar al usuario leyendo las credenciales del cuerpo
     * de la petición.
     *
     * @param request  petición HTTP con el JSON del usuario
     * @param response respuesta HTTP
     * @return objeto Authentication si las credenciales son válidas
     * @throws AuthenticationException si las credenciales son incorrectas
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = null;
        String password = null;

        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        return this.authenticationManager.authenticate(token);
    }

    /**
     * Se ejecuta si la autenticación es exitosa. Genera un JWT firmado y lo
     * devuelve al cliente.
     *
     * @param request    petición original
     * @param response   respuesta HTTP donde se añade el token
     * @param chain      cadena de filtros
     * @param authResult resultado de la autenticación
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();
        Collection<? extends GrantedAuthority> roles = user.getAuthorities();

        Claims claims = Jwts.claims()
                .add("authorities", new ObjectMapper().writeValueAsString(roles))
                .add("username", user.getUsername())
                .build();

        String jwt = Jwts.builder()
                .subject(user.getUsername())
                .claims(claims)
                .signWith(SECRET_KEY)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora de expiración
                .compact();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + jwt);

        Map<String, String> body = new HashMap<>();
        body.put("token", jwt);
        body.put("username", user.getUsername());
        body.put("message", String.format("Hola %s, has iniciado sesión con éxito", user.getUsername()));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Se ejecuta si la autenticación falla. Devuelve un mensaje de error
     * personalizado.
     *
     * @param request  petición original
     * @param response respuesta HTTP con el error
     * @param failed   excepción con el motivo del fallo
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        Map<String, String> body = new HashMap<>();
        body.put("message", "Error en la autenticación: usuario o contraseña incorrectos.");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
