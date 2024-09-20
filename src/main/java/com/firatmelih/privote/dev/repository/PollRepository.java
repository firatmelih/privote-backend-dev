package com.firatmelih.privote.dev.repository;

import com.firatmelih.privote.dev.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Integer> {

    Poll findPollById(Integer pollId);

    List<Poll> findAllByCreator_Id(Integer creatorId);

    boolean existsPollByIdAndCreatorId(Integer pollId, Integer creatorId);

    Poll findPollByIdAndCreatorId(Integer id, Integer creator_id);

    @Query("SELECT p FROM Poll p JOIN p.votes v WHERE v.voter.id = :voterId")
    List<Poll> findAllPollsAddedAsVoter(@Param("voterId") Integer voterId);

    @Query("SELECT p FROM Poll p WHERE p.creator.id = :creatorId")
    List<Poll> findAllPollsCreatedByUser(@Param("creatorId") Integer creatorId);

    @Query("SELECT p FROM Poll p LEFT JOIN FETCH p.pollOptions LEFT JOIN FETCH p.votes WHERE p.id = :id")
    Poll findOnePollByIdWithOptionsAndVotes(@Param("id") Integer id);

    void deletePollById(Integer pollId);
}


