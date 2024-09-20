package com.firatmelih.privote.dev.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PollEditRequest {
    private String title;
    private String description;
    private List<String> options;
    private List<String> voters;
}
