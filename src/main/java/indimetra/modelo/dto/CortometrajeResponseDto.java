package indimetra.modelo.dto;

import java.math.BigDecimal;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CortometrajeResponseDto {
    private Long id;
    private String title;
    private String description;
    private String technique;
    private int releaseYear;
    private int duration;
    private String language;
    private BigDecimal rating;
    private String imageUrl;
    private String videoUrl;
    private String category;
    private String author;
}
