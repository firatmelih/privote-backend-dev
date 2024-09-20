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
public class PollVoteDto {
    @NotNull(message = "Poll voter must have valid id")
    Integer voterId;
    @NotBlank(message = "Poll voter username must be provided")
    @Size(min = 5, max = 20, message = "Poll voter is type User must be between 5 and 20 characters")
    String voterUsername;
    Boolean voted;
}
