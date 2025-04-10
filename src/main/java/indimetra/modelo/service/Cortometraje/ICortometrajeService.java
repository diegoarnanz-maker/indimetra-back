package indimetra.modelo.service.Cortometraje;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.Base.IGenericDtoService;
import indimetra.modelo.service.Cortometraje.Model.CortometrajeRequestDto;
import indimetra.modelo.service.Cortometraje.Model.CortometrajeResponseDto;
import indimetra.modelo.service.Shared.Model.PagedResponse;

public interface ICortometrajeService
        extends IGenericDtoService<Cortometraje, CortometrajeRequestDto, CortometrajeResponseDto, Long> {

    Page<CortometrajeResponseDto> findAll(Pageable pageable);

    PagedResponse<CortometrajeResponseDto> findAllPaginated(Pageable pageable);

    void updateRating(Long id, BigDecimal rating);

    List<CortometrajeResponseDto> findByCategory(String category);

    List<CortometrajeResponseDto> findByRating(Double rating);

    List<CortometrajeResponseDto> findByTitleContainingIgnoreCase(String title);

    List<CortometrajeResponseDto> findLatestSeries();

    Optional<Cortometraje> findByIdIfOwnerOrAdmin(Long id, User usuario);

    List<CortometrajeResponseDto> findTopRated();

    List<CortometrajeResponseDto> findByDuracionMenorOIgual(Integer minutos);

    CortometrajeResponseDto createWithValidation(CortometrajeRequestDto dto, String username);

    CortometrajeResponseDto updateIfOwnerOrAdmin(Long id, CortometrajeRequestDto dto, String username);

    void deleteIfOwnerOrAdmin(Long id, String username);

    List<CortometrajeResponseDto> findByUsername(String username);

}
