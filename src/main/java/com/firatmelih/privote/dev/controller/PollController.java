package com.firatmelih.privote.dev.controller;

import com.firatmelih.privote.dev.request.PollCreateRequest;
import com.firatmelih.privote.dev.request.PollEditRequest;
import com.firatmelih.privote.dev.response.PollResponse;
import com.firatmelih.privote.dev.security.JwtService;
import com.firatmelih.privote.dev.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/poll")
@RestController
@RequiredArgsConstructor
public class PollController {

    private final JwtService jwtService;
    private final PollService pollService;

    //CREATE
    @PostMapping
    public ResponseEntity<Object> createPoll(
            @RequestBody PollCreateRequest request,
            @RequestHeader("Authorization") String tokenHeader) {
        return pollService.createPoll(request, tokenHeader);
    }

    //READ
    @GetMapping("/{pollId}")
    public ResponseEntity<PollResponse> getPollById(@PathVariable("pollId") Integer pollId) {
        return pollService.getPoll(pollId);
    }

    @GetMapping
    public ResponseEntity<List<PollResponse>> getAllPolls() {
        return pollService.getAllPolls();
    }

    //UPDATE
    @PutMapping("/{pollId}")
    public ResponseEntity<Object> updatePoll(
            @RequestBody PollEditRequest request,
            @PathVariable Integer pollId,
            @RequestHeader("Authorization") String tokenHeader) {
        return pollService.editPoll(
                request,
                pollId,
                tokenHeader
        );
    }

    //DELETE
    @DeleteMapping("/{pollId}")
    public ResponseEntity<Object> deletePoll(
            @PathVariable Integer pollId,
            @RequestHeader("Authorization") String tokenHeader) {
        return pollService.deletePoll(pollId, tokenHeader);
    }


    @GetMapping("/added")
    public ResponseEntity<List<PollResponse>> getAddedPolls(@RequestHeader(name = "Authorization") String header) {
        return pollService.getUserAddedPolls(header);
    }

    @GetMapping("/created")
    public ResponseEntity<List<PollResponse>> getCreatedPolls(@RequestHeader(name = "Authorization") String header) {
        return pollService.getUserCreatedPolls(header);
    }
}
