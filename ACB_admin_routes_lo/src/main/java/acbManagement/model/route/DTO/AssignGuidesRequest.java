package acbManagement.model.route.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record AssignGuidesRequest(
        @NotEmpty @Size(max = 2, message = "una ruta admite maximo dos guias")
        @Valid List<Item> guides
) {
    public record Item(
            @NotNull Long guideId,
            @Size(max = 150) String name,
            boolean primary
    ) {
    }
}
