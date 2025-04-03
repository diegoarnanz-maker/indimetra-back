package indimetra.modelo.service.Review.Model;

import java.math.BigDecimal;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {

    private Long id;

    private BigDecimal rating;

    private String comment;

    private String username;

    private Long cortometrajeId;

    private String cortometrajeTitle;
}
