package com.firatmelih.privote.dev.response;

import com.firatmelih.privote.dev.dto.PollCreatorDto;
import com.firatmelih.privote.dev.dto.PollVoteDto;
import com.firatmelih.privote.dev.model.Poll_Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PollResponse {
    Integer id;
    String title;
    String description;
    PollCreatorDto creator;
    List<Poll_Option> options;
    List<PollVoteDto> votes;
}
