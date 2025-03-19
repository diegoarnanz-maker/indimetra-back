package indimetra.config;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import indimetra.modelo.dto.UserResponseDto;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<Set<Role>, Set<String>> roleConverter = ctx ->
                ctx.getSource().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet());

        modelMapper.typeMap(User.class, UserResponseDto.class)
                .addMappings(new PropertyMap<User, UserResponseDto>() {
                    @Override
                    protected void configure() {
                        using(roleConverter).map(source.getRoles(), destination.getRoles());
                    }
                });

        return modelMapper;
    }
}
