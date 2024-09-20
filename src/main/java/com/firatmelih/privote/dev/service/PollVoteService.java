package com.firatmelih.privote.dev.service;

import com.firatmelih.privote.dev.dto.PollVoteDto;
import com.firatmelih.privote.dev.model.Poll;
import com.firatmelih.privote.dev.model.Poll_Vote;
import com.firatmelih.privote.dev.model.User;
import com.firatmelih.privote.dev.repository.PollVoteRepository;
import com.firatmelih.privote.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PollVoteService {
    private final PollVoteRepository pollVoteRepository;
    private final UserRepository userRepository;

    public void savePollVoters(
            Poll poll,
            List<String> voterUsernames) {

        if (poll.getVotes() == null) {
            poll.setVotes(new ArrayList<>());
        }

        List<String> uniqueVoterUsernames = voterUsernames.stream().distinct().toList();
        List<User> voters = userRepository.findByUsernameIn(uniqueVoterUsernames);

        for (User voter : voters) {
            Poll_Vote vote = Poll_Vote.builder()
                    .voter(voter)
                    .poll(poll)
                    .isVoted(false)
                    .build();
            var createdVote = pollVoteRepository.save(vote);
            poll.getVotes().add(createdVote);
        }
    }

    public void editPollVotes(Poll poll, List<String> voterUsernames) {
        poll.getVotes().clear();
        savePollVoters(poll, voterUsernames);
    }

    List<PollVoteDto> getPollVotes(Poll poll) {

        List<PollVoteDto> voterList = new ArrayList<>();

        for (Poll_Vote vote : poll.getVotes()) {
            voterList.add(
                    PollVoteDto.builder()
                            .voterId(vote.getVoter().getId())
                            .voterUsername(vote.getVoter().getUsername())
                            .voted(vote.isVoted())
                            .build()
            );
        }

        return voterList;
    }

}
