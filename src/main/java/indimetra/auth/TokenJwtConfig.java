package indimetra.auth;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

/**
 * Clase de configuración central para la gestión de tokens JWT.
 * 
 * Define constantes reutilizables para cabeceras, prefijos y generación de la
 * clave secreta.
 */
public class TokenJwtConfig {

    /**
     * Tipo de contenido usado en respuestas relacionadas con autenticación.
     */
    public static final String CONTENT_TYPE = "application/json";

    /**
     * Prefijo estándar usado en la cabecera Authorization para los tokens JWT.
     * Ejemplo: "Bearer eyJhbGciOiJIUzI1NiJ9..."
     */
    public static final String PREFIX_TOKEN = "Bearer ";

    /**
     * Nombre de la cabecera HTTP usada para enviar el token JWT.
     */
    public static final String HEADER_AUTHORIZATION = "Authorization";

    /**
     * Clave secreta utilizada para firmar y verificar tokens JWT.
     * 
     * Se genera automáticamente con un algoritmo HS256 seguro.
     */
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
}
