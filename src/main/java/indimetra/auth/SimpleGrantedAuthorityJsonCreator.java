package indimetra.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mixin de Jackson utilizado para deserializar correctamente los roles
 * (autoridades)
 * del token JWT hacia objetos `SimpleGrantedAuthority`.
 * 
 * <p>
 * Spring Security usa `SimpleGrantedAuthority` para manejar los roles.
 * Al deserializar un JWT que contiene una colección de roles, este mixin
 * permite que Jackson los interprete correctamente.
 * </p>
 */
public abstract class SimpleGrantedAuthorityJsonCreator {

    /**
     * Constructor requerido por Jackson para deserializar la propiedad "authority"
     * presente en el token JWT.
     * 
     * @param role el nombre del rol o autoridad, por ejemplo "ROLE_USER"
     */
    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role) {
    }
}
