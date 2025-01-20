package ma.hariti.asmaa.wrm.inventivit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCaseDTO(
        @NotBlank(message = "Title cannot be blank")
        @Size(max = 255, message = "Title cannot be longer than 255 characters")
        String title,

        @NotBlank(message = "Description cannot be blank")
        @Size(max = 2056, message = "Description cannot be longer than 2056 characters")
        String description
) {

}
