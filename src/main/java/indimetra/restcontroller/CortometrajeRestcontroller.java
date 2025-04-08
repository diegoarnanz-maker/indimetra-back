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
import org.springframework.data.domain.Pageable;

import indimetra.exception.ForbiddenException;
import indimetra.exception.NotFoundException;
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
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cortometraje")
public class CortometrajeRestcontroller extends BaseRestcontroller {

        @Autowired
        private ICortometrajeService cortometrajeService;

        @Autowired
        private IUserService userService;

        @Autowired
        private ICategoryService categoryService;

        @Autowired
        private ModelMapper modelMapper;

        @GetMapping
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> findAll() {
                List<CortometrajeResponseDto> response = cortometrajeService.findAll()
                                .stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return success(response, "Listado de cortometrajes");
        }

        @GetMapping("/paginated")
        public ResponseEntity<PagedResponse<CortometrajeResponseDto>> findAllPaginated(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                Pageable pageable = PageRequest.of(page, size);
                Page<Cortometraje> pageResult = cortometrajeService.findAll(pageable);

                List<CortometrajeResponseDto> dtoList = pageResult.getContent().stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                PagedResponse<CortometrajeResponseDto> response = PagedResponse.<CortometrajeResponseDto>builder()
                                .message("Listado de cortometrajes paginado")
                                .data(dtoList)
                                .totalItems((int) pageResult.getTotalElements())
                                .page(pageResult.getNumber())
                                .pageSize(pageResult.getSize())
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<CortometrajeResponseDto>> read(@PathVariable Long id) {
                Cortometraje cortometraje = cortometrajeService.read(id)
                                .orElseThrow(() -> new NotFoundException("Cortometraje no encontrado con ID: " + id));

                CortometrajeResponseDto response = modelMapper.map(cortometraje, CortometrajeResponseDto.class);
                return success(response, "Cortometraje encontrado");
        }

        @GetMapping("/buscar/{title}")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> buscarPorNombre(@PathVariable String title) {
                List<Cortometraje> cortos = cortometrajeService.findByTitleContainingIgnoreCase(title);

                if (cortos.isEmpty()) {
                        throw new NotFoundException("No se encontraron cortometrajes que coincidan con: " + title);
                }

                List<CortometrajeResponseDto> response = cortos.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return success(response, "Resultados para el título: " + title);
        }

        @GetMapping("/buscar/categoria/{categoryName}")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> buscarPorCategoria(
                        @PathVariable String categoryName) {
                List<Cortometraje> cortos = cortometrajeService.findByCategory(categoryName);

                if (cortos.isEmpty()) {
                        throw new NotFoundException("No se encontraron cortometrajes en la categoría: " + categoryName);
                }

                List<CortometrajeResponseDto> response = cortos.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return success(response, "Cortometrajes encontrados en la categoría: " + categoryName);
        }

        @GetMapping("/buscar/latest")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> obtenerTop5Nuevos() {
                List<Cortometraje> cortos = cortometrajeService.findLatestSeries();

                List<CortometrajeResponseDto> response = cortos.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return success(response, "Top 5 más recientes");
        }

        // {{Valor}} solo numeros enteros
        @GetMapping("/buscar/rating-minimo/{valor}")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> obtenerCortosConMinimoRating(
                        @PathVariable Double valor) {
                List<Cortometraje> cortos = cortometrajeService.findByRating(valor);

                if (cortos.isEmpty()) {
                        throw new NotFoundException("No se encontraron cortometrajes con rating >= " + valor);
                }

                List<CortometrajeResponseDto> response = cortos.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return success(response, "Cortometrajes con rating mayor o igual a " + valor);
        }

        @GetMapping("/buscar/top5-mejor-valorados")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> obtenerTop5MejorValorados() {
                List<Cortometraje> top = cortometrajeService.findTopRated();

                List<CortometrajeResponseDto> response = top.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return success(response, "Top 5 mejor valorados");
        }

        @GetMapping("/buscar/duracion-maxima/{minutos}")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> buscarPorDuracionMaxima(
                        @PathVariable Integer minutos) {
                List<Cortometraje> cortos = cortometrajeService.findByDuracionMenorOIgual(minutos);

                if (cortos.isEmpty()) {
                        throw new NotFoundException("No se encontraron cortometrajes con duración menor o igual a "
                                        + minutos + " minutos");
                }

                List<CortometrajeResponseDto> response = cortos.stream()
                                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                                .toList();

                return success(response, "Cortometrajes con duración menor o igual a " + minutos + " minutos");
        }

        @PostMapping
        public ResponseEntity<ApiResponse<CortometrajeResponseDto>> create(
                        @RequestBody @Valid CortometrajeRequestDto cortometrajeDto,
                        Authentication authentication) {

                User autor = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new NotFoundException(
                                                "Usuario no encontrado: " + authentication.getName()));

                Category categoria = categoryService.findByName(cortometrajeDto.getCategory())
                                .orElseThrow(() -> new NotFoundException(
                                                "Categoría no encontrada: " + cortometrajeDto.getCategory()));

                Cortometraje cortometraje = modelMapper.map(cortometrajeDto, Cortometraje.class);
                cortometraje.setUser(autor);
                cortometraje.setCategory(categoria);

                Cortometraje nuevo = cortometrajeService.create(cortometraje);

                // Si es ROLE_USER y no es autor aún, lo marcamos como autor
                boolean esUsuario = autor.getRoles().stream()
                                .anyMatch(rol -> rol.getName().equals(Role.RoleType.ROLE_USER));
                if (esUsuario && !autor.getIsAuthor()) {
                        userService.updateAuthorStatus(autor.getId(), true);
                }

                CortometrajeResponseDto response = modelMapper.map(nuevo, CortometrajeResponseDto.class);
                response.setAuthor(autor.getUsername());
                response.setCategory(categoria.getName());

                return created(response, "Cortometraje creado correctamente");
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<CortometrajeResponseDto>> update(
                        @PathVariable Long id,
                        @RequestBody @Valid CortometrajeRequestDto cortometrajeDto,
                        Authentication authentication) {

                Cortometraje existente = cortometrajeService.read(id)
                                .orElseThrow(() -> new NotFoundException("Cortometraje no encontrado con ID: " + id));

                User autor = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new NotFoundException(
                                                "Usuario no encontrado: " + authentication.getName()));

                boolean esPropietario = existente.getUser().getId().equals(autor.getId());
                boolean esAdmin = autor.getRoles().stream()
                                .anyMatch(rol -> rol.getName().equals(Role.RoleType.ROLE_ADMIN));

                if (!esPropietario && !esAdmin) {
                        throw new ForbiddenException("No tienes permisos para actualizar este cortometraje");
                }

                modelMapper.map(cortometrajeDto, existente);

                Category categoria = categoryService.findByName(cortometrajeDto.getCategory())
                                .orElseThrow(() -> new NotFoundException(
                                                "Categoría no encontrada: " + cortometrajeDto.getCategory()));

                existente.setCategory(categoria);

                Cortometraje actualizado = cortometrajeService.update(existente);

                CortometrajeResponseDto response = modelMapper.map(actualizado, CortometrajeResponseDto.class);
                response.setAuthor(autor.getUsername());
                response.setCategory(categoria.getName());

                return success(response, "Cortometraje actualizado correctamente");
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Authentication authentication) {
                User user = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new NotFoundException(
                                                "Usuario no encontrado: " + authentication.getName()));

                cortometrajeService.findByIdIfOwnerOrAdmin(id, user)
                                .orElseThrow(() -> new ForbiddenException(
                                                "No tienes permisos para eliminar este cortometraje"));

                cortometrajeService.delete(id);
                return success(null, "Cortometraje eliminado correctamente");
        }

}
