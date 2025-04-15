package indimetra.modelo.service.Cortometraje;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
import indimetra.exception.ForbiddenException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.Category;
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Favorite;
import indimetra.modelo.entity.Review;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;
import indimetra.modelo.repository.ICortometrajeRepository;
import indimetra.modelo.repository.IFavoriteRepository;
import indimetra.modelo.repository.IReviewRepository;
import indimetra.modelo.service.Base.GenericDtoServiceImpl;
import indimetra.modelo.service.Category.ICategoryService;
import indimetra.modelo.service.Cortometraje.Model.CortometrajeRequestDto;
import indimetra.modelo.service.Cortometraje.Model.CortometrajeResponseDto;
import indimetra.modelo.service.Shared.Model.PagedResponse;
import indimetra.modelo.service.User.IUserService;

@Service
public class CortometrajeServiceImplMy8
        extends GenericDtoServiceImpl<Cortometraje, CortometrajeRequestDto, CortometrajeResponseDto, Long>
        implements ICortometrajeService {

    @Autowired
    private ICortometrajeRepository cortometrajeRepository;

    @Autowired
    private IReviewRepository reviewRepository;

    @Autowired
    private IFavoriteRepository favoriteRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    protected ICortometrajeRepository getRepository() {
        return cortometrajeRepository;
    }

    @Override
    protected Class<Cortometraje> getEntityClass() {
        return Cortometraje.class;
    }

    @Override
    protected Class<CortometrajeRequestDto> getRequestDtoClass() {
        return CortometrajeRequestDto.class;
    }

    @Override
    protected Class<CortometrajeResponseDto> getResponseDtoClass() {
        return CortometrajeResponseDto.class;
    }

    @Override
    protected void setEntityId(Cortometraje entity, Long id) {
        entity.setId(id);
    }

    // ============================================================
    // 🔍 BÚSQUEDA Y LECTURA
    // ============================================================
    @Override
    public List<CortometrajeResponseDto> findAll() {
        List<Cortometraje> cortosVisibles = cortometrajeRepository.findAllVisible();

        return cortosVisibles.stream()
                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                .toList();
    }

    @Override
    public CortometrajeResponseDto findById(Long id) {
        Cortometraje entity = readEntityById(id);

        if (entity.getIsDeleted() || !entity.getIsActive() || !entity.getUser().getIsActive()) {
            throw new NotFoundException("Cortometraje no disponible");
        }

        return modelMapper.map(entity, CortometrajeResponseDto.class);
    }

    @Override
    public PagedResponse<CortometrajeResponseDto> findAllPaginated(Pageable pageable) {
        Page<Cortometraje> pageResult = cortometrajeRepository.findAllVisible(pageable);

        List<CortometrajeResponseDto> dtoList = pageResult.getContent().stream()
                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                .toList();

        return PagedResponse.<CortometrajeResponseDto>builder()
                .message("Listado de cortometrajes paginado")
                .data(dtoList)
                .totalItems((int) pageResult.getTotalElements())
                .page(pageResult.getNumber())
                .pageSize(pageResult.getSize())
                .build();
    }

    @Override
    public Page<CortometrajeResponseDto> findAll(Pageable pageable) {
        return cortometrajeRepository.findAllVisible(pageable)
                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class));
    }

    @Override
    public List<CortometrajeResponseDto> findByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        }

        List<Cortometraje> cortos = cortometrajeRepository
                .findByCategoryNameIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(category);
        if (cortos.isEmpty())
            throw new NotFoundException("No se encontraron cortometrajes en la categoría: " + category);
        return cortos.stream().map(c -> modelMapper.map(c, CortometrajeResponseDto.class)).toList();
    }

    @Override
    public List<CortometrajeResponseDto> findByRating(Double rating) {
        if (rating == null || rating < 0) {
            throw new BadRequestException("El rating debe ser mayor o igual que 0");
        }

        List<Cortometraje> resultados = cortometrajeRepository
                .findByRatingGreaterThanEqual(BigDecimal.valueOf(rating));

        List<CortometrajeResponseDto> filtrados = resultados.stream()
                .filter(c -> c.getIsActive() && !c.getIsDeleted())
                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                .toList();

        if (filtrados.isEmpty()) {
            throw new NotFoundException("No se encontraron cortometrajes con rating >= " + rating);
        }

        return filtrados;
    }

    @Override
    public List<CortometrajeResponseDto> findByTitleContainingIgnoreCase(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }

        List<Cortometraje> cortometrajes = cortometrajeRepository
                .findByTitleContainingIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(title);

        if (cortometrajes.isEmpty()) {
            throw new NotFoundException("No se encontraron cortometrajes con el título: " + title);
        }

        return cortometrajes.stream()
                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                .toList();
    }

    @Override
    public List<CortometrajeResponseDto> findLatestSeries() {
        return cortometrajeRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .filter(c -> c.getIsActive() && !c.getIsDeleted() && c.getUser().getIsActive())
                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Cortometraje> findByIdIfOwnerOrAdmin(Long id, User usuario) {
        Cortometraje cortometraje = readEntityById(id);

        boolean esPropietario = cortometraje.getUser().getId().equals(usuario.getId());
        boolean esAdmin = usuario.getRoles().stream()
                .anyMatch(r -> r.getName().equals(Role.RoleType.ROLE_ADMIN));

        if (esPropietario || esAdmin) {
            return Optional.of(cortometraje);
        }

        throw new ForbiddenException("No tienes permisos para acceder a este cortometraje");
    }

    @Override
    public List<CortometrajeResponseDto> findTopRated() {
        return cortometrajeRepository.findTop5ByOrderByRatingDesc().stream()
                .filter(c -> c.getIsActive() && !c.getIsDeleted())
                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CortometrajeResponseDto> findByDuracionMenorOIgual(Integer minutos) {
        if (minutos == null || minutos <= 0) {
            throw new BadRequestException("La duración debe ser mayor que 0");
        }

        List<Cortometraje> resultados = cortometrajeRepository.findByDurationLessThanEqual(minutos);

        List<CortometrajeResponseDto> filtrados = resultados.stream()
                .filter(c -> c.getIsActive() && !c.getIsDeleted())
                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                .collect(Collectors.toList());

        if (filtrados.isEmpty()) {
            throw new NotFoundException("No se encontraron cortometrajes con duración <= " + minutos);
        }

        return filtrados;
    }

    @Override
    public List<CortometrajeResponseDto> findByUsername(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + username));

        List<Cortometraje> lista = cortometrajeRepository.findByUserAndIsActiveTrueAndIsDeletedFalse(user);

        if (lista.isEmpty()) {
            throw new NotFoundException("No tienes cortometrajes activos creados aún.");
        }

        return lista.stream()
                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                .toList();
    }

    @Override
    public List<CortometrajeResponseDto> findByAuthor(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + username));

        List<Cortometraje> lista = cortometrajeRepository.findByUserAndIsActiveTrueAndIsDeletedFalse(user);

        if (lista.isEmpty()) {
            throw new NotFoundException("Este autor no tiene cortometrajes disponibles.");
        }

        return lista.stream()
                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                .toList();
    }

    @Override
    public List<CortometrajeResponseDto> findByLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new BadRequestException("El idioma no puede estar vacío.");
        }

        List<Cortometraje> lista = cortometrajeRepository
                .findByLanguageIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(language);

        if (lista.isEmpty()) {
            throw new NotFoundException("No se encontraron cortometrajes en el idioma: " + language);
        }

        return lista.stream()
                .map(c -> modelMapper.map(c, CortometrajeResponseDto.class))
                .toList();
    }

    // ============================================================
    // 🔧 ACTUALIZACIÓN Y GESTIÓN
    // ============================================================
    @Override
    public void updateRating(Long id, BigDecimal rating) {
        cortometrajeRepository.updateRating(id, rating);
    }

    @Override
    public CortometrajeResponseDto createWithValidation(CortometrajeRequestDto dto, String username) {
        User autor = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + username));

        Category categoria = categoryService.findByName(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + dto.getCategory()));

        if (cortometrajeRepository.existsByTitle(dto.getTitle())) {
            throw new BadRequestException("Ya existe un cortometraje con el título: " + dto.getTitle());
        }

        Cortometraje entity = modelMapper.map(dto, Cortometraje.class);
        entity.setUser(autor);
        entity.setCategory(categoria);

        Cortometraje saved = cortometrajeRepository.save(entity);

        boolean esUsuario = autor.getRoles().stream()
                .anyMatch(r -> r.getName().equals(Role.RoleType.ROLE_USER));

        if (esUsuario && !autor.getIsAuthor()) {
            userService.updateAuthorStatus(autor.getId(), true);
        }

        return modelMapper.map(saved, CortometrajeResponseDto.class);
    }

    @Override
    public CortometrajeResponseDto updateIfOwnerOrAdmin(Long id, CortometrajeRequestDto dto, String username) {
        User autor = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + username));

        Cortometraje cortometraje = findByIdIfOwnerOrAdmin(id, autor)
                .orElseThrow(() -> new ForbiddenException("No tienes permisos para actualizar este cortometraje"));

        Category categoria = categoryService.findByName(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + dto.getCategory()));

        if (cortometrajeRepository.existsByTitleAndIdNot(dto.getTitle(), id)) {
            throw new BadRequestException("Ya existe otro cortometraje con el título: " + dto.getTitle());
        }

        modelMapper.map(dto, cortometraje);
        cortometraje.setCategory(categoria);

        Cortometraje actualizado = cortometrajeRepository.save(cortometraje);
        return modelMapper.map(actualizado, CortometrajeResponseDto.class);
    }

    // ============================================================
    // 🗑️ ELIMINACIÓN Y RESTAURACIÓN
    // ============================================================
    @Override
    public void deleteIfOwnerOrAdmin(Long id, String username) {
        User usuario = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + username));

        Cortometraje cortometraje = findByIdIfOwnerOrAdmin(id, usuario)
                .orElseThrow(() -> new ForbiddenException("No tienes permisos para eliminar este cortometraje"));

        // Marcar cortometraje como eliminado
        cortometraje.setIsDeleted(true);
        cortometraje.setIsActive(false);
        cortometrajeRepository.save(cortometraje);

        // Marcar reviews como eliminadas
        List<Review> reviews = reviewRepository.findByCortometrajeId(cortometraje.getId());
        for (Review review : reviews) {
            review.setIsDeleted(true);
            review.setIsActive(false);
        }
        reviewRepository.saveAll(reviews);

        // Marcar favoritos como eliminados
        List<Favorite> favoritos = favoriteRepository.findByCortometrajeId(cortometraje.getId());
        for (Favorite fav : favoritos) {
            fav.setIsDeleted(true);
            fav.setIsActive(false);
        }
        favoriteRepository.saveAll(favoritos);

        // Comprobar si el usuario debe dejar de ser autor
        boolean tieneMasCortometrajes = cortometrajeRepository.existsByUserIdAndIsDeletedFalse(usuario.getId());
        if (!tieneMasCortometrajes && usuario.getIsAuthor()) {
            userService.updateAuthorStatus(usuario.getId(), false);
        }
    }

}
