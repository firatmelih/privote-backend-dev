package com.firatmelih.privote.dev.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {
    private Integer pollId; // todo add validations
    private Integer optionId;
}
