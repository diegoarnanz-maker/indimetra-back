package indimetra.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import indimetra.modelo.dto.CortometrajeResponseDto;
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.service.ICortometrajeService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cortometraje")
public class CortometrajeRestcontroller {

    @Autowired
    private ICortometrajeService cortometrajeService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<CortometrajeResponseDto>> findAll() {
        List<Cortometraje> cortometrajes = cortometrajeService.findAll();

        List<CortometrajeResponseDto> response = cortometrajes.stream()
                .map(cortometraje -> modelMapper.map(cortometraje, CortometrajeResponseDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<CortometrajeResponseDto> read(@PathVariable Long id) {
        Cortometraje cortometraje = cortometrajeService.read(id)
                .orElseThrow(() -> new RuntimeException("Cortometraje no encontrado"));

        CortometrajeResponseDto response = modelMapper.map(cortometraje, CortometrajeResponseDto.class);

        return ResponseEntity.ok(response);
    }

    // @PostMapping

    // @PutMapping("{id}")

    // @DeleteMapping("{id}")

}
