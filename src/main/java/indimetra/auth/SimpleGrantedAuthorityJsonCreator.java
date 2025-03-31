package indimetra.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// Este mixin sirve para que Jackson pueda deserializar correctamente los roles desde el JWT.
public abstract class SimpleGrantedAuthorityJsonCreator {

    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role) {
    }
}
