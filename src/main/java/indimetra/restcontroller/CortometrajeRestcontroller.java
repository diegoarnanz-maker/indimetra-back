package indimetra.restcontroller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import indimetra.modelo.entity.Category;
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.Category.ICategoryService;
import indimetra.modelo.service.Cortometraje.ICortometrajeService;
import indimetra.modelo.service.Cortometraje.Model.CortometrajeRequestDto;
import indimetra.modelo.service.Cortometraje.Model.CortometrajeResponseDto;
import indimetra.modelo.service.Shared.Model.PagedResponse;
import indimetra.modelo.service.User.IUserService;
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

        @GetMapping("/paginated")
        public ResponseEntity<PagedResponse<CortometrajeResponseDto>> findAllPaginated(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                Pageable pageable = PageRequest.of(page, size);
                Page<Cortometraje> pageResult = cortometrajeService.findAll(pageable);

                List<CortometrajeResponseDto> dtoList = pageResult.getContent().stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .collect(Collectors.toList());

                PagedResponse<CortometrajeResponseDto> response = PagedResponse.<CortometrajeResponseDto>builder()
                                .message("Listado de cortometrajes paginado")
                                .data(dtoList)
                                .totalItems((int) pageResult.getTotalElements())
                                .page(pageResult.getNumber())
                                .pageSize(pageResult.getSize())
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("{id}")
        public ResponseEntity<CortometrajeResponseDto> read(@PathVariable Long id) {
                Cortometraje cortometraje = cortometrajeService.read(id)
                                .orElseThrow(() -> new RuntimeException("Cortometraje no encontrado"));

                CortometrajeResponseDto response = modelMapper.map(cortometraje, CortometrajeResponseDto.class);

                return ResponseEntity.status(200).body(response);
        }

        @GetMapping("/buscar/{title}")
        public ResponseEntity<List<CortometrajeResponseDto>> buscarPorNombre(@PathVariable String title) {
                List<Cortometraje> cortometrajes = cortometrajeService.findByTitleContainingIgnoreCase(title);

                if (cortometrajes.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "No se encontraron cortometrajes que coincidan con: " + title);
                }

                List<CortometrajeResponseDto> responseList = cortometrajes.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return ResponseEntity.ok(responseList);
        }

        @GetMapping("/buscar/categoria/{categoryName}")
        public ResponseEntity<List<CortometrajeResponseDto>> buscarPorCategoria(@PathVariable String categoryName) {
                List<Cortometraje> cortos = cortometrajeService.findByCategory(categoryName);

                if (cortos.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "No se encontraron cortometrajes en la categoría: " + categoryName);
                }

                List<CortometrajeResponseDto> response = cortos.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/buscar/latest")
        public ResponseEntity<List<CortometrajeResponseDto>> obtenerTop5Nuevos() {
                List<Cortometraje> cortos = cortometrajeService.findLatestSeries();

                List<CortometrajeResponseDto> response = cortos.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return ResponseEntity.ok(response);
        }

        // {{Valor}} solo numeros enteros
        @GetMapping("/buscar/rating-minimo/{valor}")
        public ResponseEntity<List<CortometrajeResponseDto>> obtenerCortometrajesConRatingMinimo(
                        @PathVariable Double valor) {
                List<Cortometraje> cortos = cortometrajeService.findByRating(valor);

                if (cortos.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "No se encontraron cortometrajes con rating >= " + valor);
                }

                List<CortometrajeResponseDto> response = cortos.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/buscar/top5-mejor-valorados")
        public ResponseEntity<List<CortometrajeResponseDto>> obtenerTop5MejorValorados() {
                List<Cortometraje> top = cortometrajeService.findTopRated();

                List<CortometrajeResponseDto> response = top.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/buscar/duracion-maxima/{minutos}")
        public ResponseEntity<List<CortometrajeResponseDto>> buscarPorDuracionMaxima(@PathVariable Integer minutos) {
                List<Cortometraje> cortos = cortometrajeService.findByDuracionMenorOIgual(minutos);

                if (cortos.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "No se encontraron cortometrajes con duración menor o igual a " + minutos
                                                        + " minutos");
                }

                List<CortometrajeResponseDto> response = cortos.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return ResponseEntity.ok(response);
        }

        @PostMapping
        public ResponseEntity<CortometrajeResponseDto> create(
                        @RequestBody @Valid CortometrajeRequestDto cortometrajeDto,
                        Authentication authentication) {

                User autor = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException(
                                                "Usuario no encontrado: " + authentication.getName()));

                Cortometraje cortometraje = modelMapper.map(cortometrajeDto, Cortometraje.class);

                cortometraje.setUser(autor);

                Category categoria = categoryService.findByName(cortometrajeDto.getCategory())
                                .orElseThrow(() -> new RuntimeException(
                                                "Categoría no encontrada: " + cortometrajeDto.getCategory()));

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
                                .orElseThrow(() -> new RuntimeException(
                                                "Usuario no encontrado: " + authentication.getName()));

                boolean esPropietario = cortometrajeExistente.getUser().getId().equals(autor.getId());
                boolean esAdmin = autor.getRoles().stream()
                                .anyMatch(rol -> rol.getName().equals(Role.RoleType.ROLE_ADMIN));

                if (!esPropietario && !esAdmin) {
                        return ResponseEntity.status(403).body(null);
                }

                modelMapper.map(cortometrajeDto, cortometrajeExistente);

                Category categoria = categoryService.findByName(cortometrajeDto.getCategory())
                                .orElseThrow(() -> new RuntimeException(
                                                "Categoría no encontrada: " + cortometrajeDto.getCategory()));

                cortometrajeExistente.setCategory(categoria);

                Cortometraje cortometrajeActualizado = cortometrajeService.update(cortometrajeExistente);

                CortometrajeResponseDto response = modelMapper.map(cortometrajeActualizado,
                                CortometrajeResponseDto.class);
                response.setAuthor(autor.getUsername());
                response.setCategory(categoria.getName());

                return ResponseEntity.status(200).body(response);
        }

        @DeleteMapping("{id}")
        public ResponseEntity<Map<String, String>> delete(@PathVariable Long id, Authentication authentication) {
                User user = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                cortometrajeService.findByIdIfOwnerOrAdmin(id, user)
                                .orElseThrow(() -> new RuntimeException(
                                                "No tienes permisos para eliminar este cortometraje"));

                cortometrajeService.delete(id);

                return ResponseEntity.ok(Map.of("message", "Cortometraje eliminado correctamente"));
        }

}
