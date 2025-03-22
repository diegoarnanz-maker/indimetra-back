package indimetra.modelo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;
}
