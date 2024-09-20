package com.firatmelih.privote.dev.service;

import com.firatmelih.privote.dev.dto.PollCreatorDto;
import com.firatmelih.privote.dev.model.Poll;
import com.firatmelih.privote.dev.model.Poll_Option;
import com.firatmelih.privote.dev.model.User;
import com.firatmelih.privote.dev.repository.PollOptionRepository;
import com.firatmelih.privote.dev.repository.PollRepository;
import com.firatmelih.privote.dev.repository.PollVoteRepository;
import com.firatmelih.privote.dev.repository.UserRepository;
import com.firatmelih.privote.dev.request.PollCreateRequest;
import com.firatmelih.privote.dev.request.PollEditRequest;
import com.firatmelih.privote.dev.response.PollResponse;
import com.firatmelih.privote.dev.security.JwtService;
import com.firatmelih.privote.dev.utility.ResponseUtility;
import jakarta.transaction.Transactional;
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
public class PollService {

    private final UserRepository userRepository;
    private final PollRepository pollRepository;
    private final PollOptionService pollOptionService;
    private final PollVoteService pollVoteService;
    private final JwtService jwtService;
    private final ResponseUtility responseUtility;
    private final PollVoteRepository pollVoteRepository;
    private final PollOptionRepository pollOptionRepository;
    private final UserService userService;

    @Transactional
    public ResponseEntity<Object> createPoll(
            PollCreateRequest request,
            String tokenHeader) {

        Integer creatorId = jwtService.getUserIdFromHeader(tokenHeader);
        User creator = userRepository.findUserById(creatorId);

        Poll pollToCreate = Poll.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .creator(creator)
                .build();

        Poll createdPoll = pollRepository.save(pollToCreate);

        pollOptionService.savePollOptions(
                createdPoll,
                request.getOptions());

        pollVoteService.savePollVoters(
                createdPoll,
                request.getVoters()
        );

        PollResponse response = PollResponse.builder()
                .id(createdPoll
                        .getId())
                .title(createdPoll
                        .getTitle())
                .description(createdPoll
                        .getDescription())
                .creator(userService.userToDto(creator))
                .options(createdPoll
                        .getPollOptions())
                .votes(pollVoteService
                        .getPollVotes(createdPoll))
                .build();

        return responseUtility.createResponseWithObject(
                "Poll created",
                "body",
                response,
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<List<PollResponse>> getAllPolls() {
        List<Poll> polls = pollRepository.findAll();
        List<PollResponse> response = new ArrayList<>();


        for (Poll poll : polls) {
            PollCreatorDto creatorDto = PollCreatorDto.builder()
                    .id(poll.getCreator().getId())
                    .username(poll.getCreator().getUsername())
                    .build();
            response.add(
                    PollResponse.builder()
                            .id(poll.getId())
                            .title(poll.getTitle())
                            .description(poll.getDescription())
                            .votes(pollVoteService.getPollVotes(poll))
                            .creator(creatorDto)
                            .build()
            );
        }

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<PollResponse> getPoll(Integer pollId) {

        Poll poll = pollRepository.findPollById(pollId);
        List<Poll_Option> options = pollOptionService.getPollOptions(poll);

        PollCreatorDto creatorDto = PollCreatorDto.builder()
                .id(poll.getCreator().getId())
                .username(poll.getCreator().getUsername())
                .build();

        PollResponse response = PollResponse.builder()
                .id(poll
                        .getId())
                .title(poll
                        .getTitle())
                .description(poll
                        .getDescription())
                .creator(creatorDto)
                .options(options)
                .votes(pollVoteService.getPollVotes(poll))
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<PollResponse>> getUserCreatedPolls(String tokenHeader) {
        Integer creatorId = jwtService.getUserIdFromHeader(tokenHeader);

        List<Poll> polls = pollRepository.findAllPollsCreatedByUser(creatorId);
        List<PollResponse> response = new ArrayList<>();

        User user = userRepository.findUserById(creatorId);

        PollCreatorDto creatorDto = PollCreatorDto.builder()
                .id(creatorId)
                .username(user.getUsername())
                .build();
        for (Poll poll : polls) {
            response.add(
                    PollResponse.builder()
                            .id(poll.getId())
                            .title(poll.getTitle())
                            .description(poll.getDescription())
                            .votes(pollVoteService.getPollVotes(poll))
                            .creator(creatorDto)
                            .build()
            );
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<PollResponse>> getUserAddedPolls(String tokenHeader) {
        Integer voterId = jwtService.getUserIdFromHeader(tokenHeader);

        List<Poll> polls = pollRepository.findAllPollsAddedAsVoter(voterId);
        List<PollResponse> response = new ArrayList<>();

        PollCreatorDto creatorDto = PollCreatorDto.builder()
                .id(polls.get(0).getCreator().getId())
                .username(polls.get(0).getCreator().getUsername())
                .build();
        for (Poll poll : polls) {
            List<Poll_Option> options = pollOptionService.getPollOptions(poll);
            response.add(
                    PollResponse.builder()
                            .id(poll.getId())
                            .title(poll.getTitle())
                            .description(poll.getDescription())
                            .options(options)
                            .votes(pollVoteService.getPollVotes(poll))
                            .creator(creatorDto)
                            .build()
            );
        }
        return ResponseEntity.ok(response);
    }

    //UPDATE
    @Transactional
    public ResponseEntity<Object> editPoll(
            PollEditRequest request,
            Integer pollId,
            String tokenHeader) {
        Integer userId = jwtService.getUserIdFromHeader(tokenHeader);
        if (pollRepository.existsPollByIdAndCreatorId(pollId, userId)) {
            Poll pollToEdit = pollRepository.findPollByIdAndCreatorId(pollId, userId);
            if (pollToEdit.getPollOptions().stream().anyMatch(option -> option.getCount() > 0)) {
                return responseUtility.createResponse(
                        "You can't update vote after vote began",
                        HttpStatus.METHOD_NOT_ALLOWED);
            }
            pollToEdit.setTitle(request.getDescription());
            pollToEdit.setDescription(request.getDescription());

            if (request.getOptions() != null && !request.getOptions().isEmpty()) {
                pollOptionService.editPollOptions(pollToEdit, request.getOptions());
            }
            if (request.getVoters() != null && !request.getVoters().isEmpty()) {
                pollVoteService.editPollVotes(pollToEdit, request.getVoters());
            }

            Poll updatedPoll = pollRepository.save(pollToEdit);

            PollResponse response = PollResponse.builder()
                    .id(updatedPoll.getId())
                    .title(updatedPoll.getTitle())
                    .description(updatedPoll.getDescription())
                    .creator(userService.userToDto(updatedPoll.getCreator()))
                    .options(updatedPoll.getPollOptions())
                    .votes(pollVoteService.getPollVotes(updatedPoll))
                    .build();

            return responseUtility.createResponseWithObject(
                    "Poll updated",
                    "response",
                    response,
                    HttpStatus.OK);
        } else {
            return responseUtility.createResponse("Poll not found", HttpStatus.NOT_FOUND);
        }
    }

    //DELETE
    @Transactional
    public ResponseEntity<Object> deletePoll(Integer pollId, String tokenHeader) {
        Integer userId = jwtService.getUserIdFromHeader(tokenHeader);
        if (pollRepository.existsPollByIdAndCreatorId(pollId, userId)) {
            pollRepository.deletePollById(pollId);
            return responseUtility.createResponse("Poll deleted", HttpStatus.OK);
        } else {
            return responseUtility.createResponse("Poll not found", HttpStatus.NOT_FOUND);
        }
    }
}
