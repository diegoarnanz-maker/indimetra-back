package indimetra.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import indimetra.modelo.service.Category.ICategoryService;
import indimetra.modelo.service.Category.Model.CategoryRequestDto;
import indimetra.modelo.service.Category.Model.CategoryResponseDto;
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/category")
public class CategoryRestcontroller extends BaseRestcontroller {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> findAll() {
        List<CategoryResponseDto> response = categoryService.findAll();
        return success(response, "Listado de categorías");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> findById(@PathVariable Long id) {
        CategoryResponseDto response = categoryService.findById(id);
        return success(response, "Categoría encontrada");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDto>> create(@RequestBody @Valid CategoryRequestDto dto) {
        CategoryResponseDto response = categoryService.create(dto);
        return created(response, "Categoría creada correctamente");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> update(@PathVariable Long id,
            @RequestBody @Valid CategoryRequestDto dto) {
        CategoryResponseDto response = categoryService.update(id, dto);
        return success(response, "Categoría actualizada correctamente");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return success(null, "Categoría eliminada correctamente");
    }
}
