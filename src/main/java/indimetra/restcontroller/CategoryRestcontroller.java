package indimetra.restcontroller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import indimetra.modelo.dto.CategoryRequestDto;
import indimetra.modelo.dto.CategoryResponseDto;
import indimetra.modelo.entity.Category;
import indimetra.modelo.service.ICategoryService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/category")
public class CategoryRestcontroller {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAll() {
        List<Category> categories = categoryService.findAll();

        List<CategoryResponseDto> response = categories.stream()
                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
                .toList();

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable Long id) {
        Category category = categoryService.read(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrado"));

        CategoryResponseDto response = modelMapper.map(category, CategoryResponseDto.class);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@RequestBody @Valid CategoryRequestDto dto) {
        Category category = modelMapper.map(dto, Category.class);
        Category saved = categoryService.create(category);
        CategoryResponseDto response = modelMapper.map(saved, CategoryResponseDto.class);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable Long id,
            @RequestBody @Valid CategoryRequestDto dto) {

        Category category = modelMapper.map(dto, Category.class);
        category.setId(id);

        Category updated = categoryService.update(category);
        CategoryResponseDto response = modelMapper.map(updated, CategoryResponseDto.class);

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.status(204).build();
    }

}
