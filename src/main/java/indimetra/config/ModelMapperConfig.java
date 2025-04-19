package indimetra.config;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Review;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.Cortometraje.Model.CortometrajeResponseDto;
import indimetra.modelo.service.Review.Model.ReviewResponseDto;
import indimetra.modelo.service.User.Model.UserResponseDto;

/**
 * Configuración global de ModelMapper para personalizar el mapeo entre
 * entidades y DTOs.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Bean que configura el {@link ModelMapper} con conversiones personalizadas
     * para entidades como {@link User}, {@link Cortometraje} y {@link Review}.
     *
     * @return instancia configurada de ModelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // ============================================
        // MAPEOS PERSONALIZADOS
        // ============================================

        // Conversión de Set<Role> a Set<String> (nombre del rol)
        Converter<Set<Role>, Set<String>> roleConverter = ctx -> ctx.getSource().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        // User -> UserResponseDto
        modelMapper.typeMap(User.class, UserResponseDto.class)
                .addMappings(new PropertyMap<User, UserResponseDto>() {
                    @Override
                    protected void configure() {
                        using(roleConverter).map(source.getRoles(), destination.getRoles());
                    }
                });

        // Cortometraje -> CortometrajeResponseDto
        modelMapper.typeMap(Cortometraje.class, CortometrajeResponseDto.class)
                .addMappings(new PropertyMap<Cortometraje, CortometrajeResponseDto>() {
                    @Override
                    protected void configure() {
                        map().setCategory(source.getCategory().getName());
                        map().setAuthor(source.getUser().getUsername());
                    }
                });

        // Review -> ReviewResponseDto
        modelMapper.typeMap(Review.class, ReviewResponseDto.class)
                .addMappings(new PropertyMap<Review, ReviewResponseDto>() {
                    @Override
                    protected void configure() {
                        map().setUsername(source.getUser().getUsername());
                        map().setCortometrajeId(source.getCortometraje().getId());
                        map().setCortometrajeTitle(source.getCortometraje().getTitle());
                    }
                });

        return modelMapper;
    }
}
