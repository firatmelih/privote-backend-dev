package com.firatmelih.privote.dev.repository;

import com.firatmelih.privote.dev.model.Poll_Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollVoteRepository extends JpaRepository<Poll_Vote, Integer> {
    Poll_Vote findPoll_VoteByVoterIdAndPollId(int voterId, int pollId);

    List<Poll_Vote> findByVoterIdAndPollId(Integer voterId, Integer pollId);

    List<Poll_Vote> findByVoterId(Integer voterId);

    List<Poll_Vote> findAllByPollId(Integer pollId);

    void deleteAllByPollId(Integer pollId);
}
