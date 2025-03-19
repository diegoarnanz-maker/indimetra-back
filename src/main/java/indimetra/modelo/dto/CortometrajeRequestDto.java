package indimetra.modelo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CortometrajeRequestDto {


    private String title;

    private String description;

    private String technique;

    private int releaseYear;

    private int duration;

    private String language;

    private Double rating;

    private String imageUrl;

    private String videoUrl;

    private Long userId;

    private String category;
}
