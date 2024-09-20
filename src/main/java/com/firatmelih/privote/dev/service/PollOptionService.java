package com.firatmelih.privote.dev.service;

import com.firatmelih.privote.dev.exception.PollOptionNotFound;
import com.firatmelih.privote.dev.model.Poll;
import com.firatmelih.privote.dev.model.Poll_Option;
import com.firatmelih.privote.dev.model.Poll_Vote;
import com.firatmelih.privote.dev.model.User;
import com.firatmelih.privote.dev.repository.PollOptionRepository;
import com.firatmelih.privote.dev.repository.PollVoteRepository;
import com.firatmelih.privote.dev.request.VoteRequest;
import com.firatmelih.privote.dev.security.JwtService;
import com.firatmelih.privote.dev.utility.ResponseUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PollOptionService {

    private final PollOptionRepository pollOptionRepository;
    private final JwtService jwtService;
    private final UserService userService;
    private final PollVoteRepository pollVoteRepository;
    private final ResponseUtility responseUtility;

    public void savePollOptions(Poll poll, List<String> options) {

        if (poll.getPollOptions() == null) {
            poll.setPollOptions(new ArrayList<>());
        }

        for (String optionContent : options) {
            Poll_Option pollOption = Poll_Option.builder()
                    .content(optionContent)
                    .poll(poll)
                    .count(0)
                    .build();
            var createdPollOption = pollOptionRepository.save(pollOption);
            poll.getPollOptions().add(createdPollOption);
        }
        poll.setPollOptions(poll.getPollOptions());
    }

    public void editPollOptions(Poll poll, List<String> options) {
        poll.getPollOptions().clear();
        savePollOptions(poll, options);
    }

    public List<Poll_Option> getPollOptions(Poll poll) {
        List<Poll_Option> pollOptionsList = new ArrayList<>();

        for (Poll_Option option : poll.getPollOptions()) {
            pollOptionsList.add(
                    Poll_Option.builder()
                            .id(option.getId())
                            .count(option.getCount())
                            .content(option.getContent())
                            .build()
            );
        }
        return pollOptionsList;
    }

    public ResponseEntity<Object> vote(VoteRequest request, String header) {

        String token = jwtService.getTokenFromHeader(header);
        String usernameFromToken = jwtService.extractUsername(token);
        User userFromToken = userService.getOneUserByUsername(usernameFromToken);

        Poll_Vote vote = pollVoteRepository.findPoll_VoteByVoterIdAndPollId(userFromToken.getId(), request.getPollId());
        if (vote == null) {
            return responseUtility.createResponse(
                    "User doesn't belong in vote or vote doesn't exist",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (vote.isVoted()) {
            return responseUtility.createResponse(
                    "User already voted",
                    HttpStatus.CONFLICT);
        }
        pollVoteRepository.save(vote);
        List<Poll_Option> pollOptions = pollOptionRepository.findAllByPollId(request.getPollId());
        if (pollOptions.isEmpty()) {
            return responseUtility.createResponse(
                    "Options doesn't exist",
                    HttpStatus.BAD_REQUEST
            );
        }
        if (pollOptions.stream()
                .noneMatch(option ->
                        option.getId().equals(request.getOptionId()))) {
            return responseUtility.createResponse(
                    "Option " +
                            "with ID " + request.getOptionId() +
                            " doesn't exist",
                    HttpStatus.BAD_REQUEST
            );
        }

        Poll_Option pollOption = pollOptions.stream()
                .filter(option -> option.getId().equals(request.getOptionId()))
                .findFirst()
                .orElseThrow(() -> new PollOptionNotFound("Option not found"));
        pollOption.setCount(pollOption.getCount() + 1);
        vote.setVoted(true);
        pollOptionRepository.save(pollOption);
        return responseUtility.createResponse(
                "Successfully voted",
                HttpStatus.OK);
    }

}
