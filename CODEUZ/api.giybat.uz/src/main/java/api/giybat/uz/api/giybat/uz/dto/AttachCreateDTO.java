package api.giybat.uz.api.giybat.uz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AttachCreateDTO {
    @NotNull(message = "ID required")
    private String id;
}