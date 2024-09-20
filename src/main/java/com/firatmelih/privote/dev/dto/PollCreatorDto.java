package com.firatmelih.privote.dev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PollCreatorDto {
    @NotNull(message = "Poll creator must have valid id")
    Integer id;
    @NotBlank(message = "Poll creator username must be provided")
    @Size(min = 5, max = 20, message = "Poll creator is type User must be between 5 and 20 characters")
    String username;
}
