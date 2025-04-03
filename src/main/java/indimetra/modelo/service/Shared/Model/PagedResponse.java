package indimetra.modelo.service.Shared.Model;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedResponse<T> {
    private String message;
    private List<T> data;
    private int totalItems;
    private int page;
    private int pageSize;
}
