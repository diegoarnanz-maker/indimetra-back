package indimetra.modelo.service.Favorite.Model;

import java.util.Date;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponseDto {
    private Long id;
    private Long cortometrajeId;
    private String cortometrajeTitle;
    private String username;
    private Date createdAt;
}
