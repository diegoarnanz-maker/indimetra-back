# Indimetra Backend

Plataforma de difusión de cine independiente y de autor implementada con Spring Boot. Proporciona gestión de usuarios, autenticación mediante JWT y un catálogo de cortometrajes con reseñas, favoritos y categorías.

## Requisitos

- Java 21
- Maven 3.9+
- MySQL 8

## Configuración

1. Cree una base de datos en MySQL usando el script `src/main/resources/indimetra_app.sql`.
2. Ajuste las credenciales de conexión en `src/main/resources/application.properties`:
   ```properties
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_PASSWORD
   ```

## Ejecución

Para compilar y ejecutar la aplicación utilice Maven:

```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080` por defecto.

## Documentación de la API

Se genera automáticamente con **Springdoc OpenAPI**. Una vez en ejecución, acceda a:

```
http://localhost:8080/swagger-ui/index.html
```

a fin de consultar los endpoints disponibles y probarlos.

## Pruebas

El proyecto incluye pruebas con JUnit y Mockito. Para ejecutarlas:

```bash
./mvnw test
```

## Licencia

Este proyecto se distribuye bajo la licencia MIT.
