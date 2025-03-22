package indimetra.config;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import indimetra.modelo.dto.CortometrajeResponseDto;
import indimetra.modelo.dto.UserResponseDto;
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Transformar roles de Role a Set<String>
        Converter<Set<Role>, Set<String>> roleConverter = ctx ->
                ctx.getSource().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet());

        // Mapeo de User a UserResponseDto
        modelMapper.typeMap(User.class, UserResponseDto.class)
                .addMappings(new PropertyMap<User, UserResponseDto>() {
                    @Override
                    protected void configure() {
                        using(roleConverter).map(source.getRoles(), destination.getRoles());
                    }
                });

        // Mapeo de Cortometraje a CortometrajeResponseDto
        modelMapper.typeMap(Cortometraje.class, CortometrajeResponseDto.class)
                .addMappings(new PropertyMap<Cortometraje, CortometrajeResponseDto>() {
                    @Override
                    protected void configure() {
                        map().setCategory(source.getCategory().getName());
                        map().setAuthor(source.getUser().getUsername());
                    }
                });

        return modelMapper;
    }
}
