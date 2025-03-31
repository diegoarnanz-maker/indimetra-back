package indimetra.auth;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

//Centraliza la configuración del JWT: cabeceras, prefijos y la clave secreta con la que firmamos y validamos los tokens
public class TokenJwtConfig {

    public static final String CONTENT_TYPE = "application/json";
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    // Generamos una clave secreta segura para firmar los tokens
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

}
