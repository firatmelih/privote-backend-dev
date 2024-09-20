package com.firatmelih.privote.dev.repository;

import com.firatmelih.privote.dev.model.Poll_Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollOptionRepository extends JpaRepository<Poll_Option, Integer> {
    List<Poll_Option> findAllByPollId(Integer pollId);

    void deleteByPollId(Integer pollId);

    void deleteAllByPollId(Integer pollId);

    Poll_Option findPoll_OptionByPollId(Integer pollId);
}
