package com.firatmelih.privote.dev.controller;

import com.firatmelih.privote.dev.request.VoteRequest;
import com.firatmelih.privote.dev.service.PollOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/vote")
@RestController
@RequiredArgsConstructor
public class VoteController {
    private final PollOptionService pollOptionService;

    @PostMapping("/{id}")
    public ResponseEntity<Object> vote(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("id") Integer pollId,
            @RequestBody Integer optionId) {
        VoteRequest request = VoteRequest.builder()
                .optionId(optionId)
                .pollId(pollId)
                .build();
        return pollOptionService.vote(request, tokenHeader);
    }
}
