package indimetra.restcontroller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import indimetra.modelo.dto.CortometrajeRequestDto;
import indimetra.modelo.dto.CortometrajeResponseDto;
import indimetra.modelo.entity.Category;
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.ICategoryService;
import indimetra.modelo.service.ICortometrajeService;
import indimetra.modelo.service.IUserService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cortometraje")
public class CortometrajeRestcontroller {

    @Autowired
    private ICortometrajeService cortometrajeService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<CortometrajeResponseDto>> findAll() {
        List<Cortometraje> cortometrajes = cortometrajeService.findAll();

        List<CortometrajeResponseDto> response = cortometrajes.stream()
                .map(cortometraje -> modelMapper.map(cortometraje, CortometrajeResponseDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<CortometrajeResponseDto> read(@PathVariable Long id) {
        Cortometraje cortometraje = cortometrajeService.read(id)
                .orElseThrow(() -> new RuntimeException("Cortometraje no encontrado"));

        CortometrajeResponseDto response = modelMapper.map(cortometraje, CortometrajeResponseDto.class);

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping
    public ResponseEntity<CortometrajeResponseDto> create(@RequestBody @Valid CortometrajeRequestDto cortometrajeDto,
            Authentication authentication) {

        User autor = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + authentication.getName()));

        Cortometraje cortometraje = modelMapper.map(cortometrajeDto, Cortometraje.class);

        cortometraje.setUser(autor);

        Category categoria = categoryService.findByName(cortometrajeDto.getCategory())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + cortometrajeDto.getCategory()));

        cortometraje.setCategory(categoria);

        Cortometraje nuevoCortometraje = cortometrajeService.create(cortometraje);

        boolean esUsuario = autor.getRoles().stream()
                .anyMatch(rol -> rol.getName().equals(Role.RoleType.ROLE_USER));

        if (esUsuario && !autor.getIsAuthor()) {
            userService.updateAuthorStatus(autor.getId(), true);
        }

        CortometrajeResponseDto response = modelMapper.map(nuevoCortometraje, CortometrajeResponseDto.class);
        response.setAuthor(autor.getUsername());
        response.setCategory(categoria.getName());

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<CortometrajeResponseDto> update(@PathVariable Long id,
            @RequestBody @Valid CortometrajeRequestDto cortometrajeDto,
            Authentication authentication) {

        Cortometraje cortometrajeExistente = cortometrajeService.read(id)
                .orElseThrow(() -> new RuntimeException("Cortometraje no encontrado con ID: " + id));

        User autor = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + authentication.getName()));

        boolean esPropietario = cortometrajeExistente.getUser().getId().equals(autor.getId());
        boolean esAdmin = autor.getRoles().stream()
                .anyMatch(rol -> rol.getName().equals(Role.RoleType.ROLE_ADMIN));

        if (!esPropietario && !esAdmin) {
            return ResponseEntity.status(403).body(null);
        }

        modelMapper.map(cortometrajeDto, cortometrajeExistente);

        Category categoria = categoryService.findByName(cortometrajeDto.getCategory())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + cortometrajeDto.getCategory()));

        cortometrajeExistente.setCategory(categoria);

        Cortometraje cortometrajeActualizado = cortometrajeService.update(cortometrajeExistente);

        CortometrajeResponseDto response = modelMapper.map(cortometrajeActualizado, CortometrajeResponseDto.class);
        response.setAuthor(autor.getUsername());
        response.setCategory(categoria.getName());

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id, Authentication authentication) {

        Cortometraje cortometrajeExistente = cortometrajeService.read(id)
                .orElseThrow(() -> new RuntimeException("Cortometraje no encontrado con ID: " + id));

        User usuarioAutenticado = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + authentication.getName()));

        boolean esPropietario = cortometrajeExistente.getUser().getId().equals(usuarioAutenticado.getId());
        boolean esAdmin = usuarioAutenticado.getRoles().stream()
                .anyMatch(rol -> rol.getName().equals(Role.RoleType.ROLE_ADMIN));

        if (!esPropietario && !esAdmin) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", "No tienes permisos para eliminar este cortometraje"));
        }

        cortometrajeService.delete(id);

        return ResponseEntity.status(200).body(Map.of("message", "Cortometraje eliminado correctamente"));
    }
}
