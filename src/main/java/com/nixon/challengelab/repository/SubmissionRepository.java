package com.nixon.challengelab.repository;

import com.nixon.challengelab.model.Submission;
import com.nixon.challengelab.model.enums.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findAllByChallengeId(Long challengeId);

    List<Submission> findAllByTeamId(Long teamId);

    Boolean existsByTeamIdAndChallengeId(Long teamId, Long challengeId);

    List<Submission> findAllByUserId(Long userId);

    boolean existsByStatusAndChallengeId(SubmissionStatus status, Long challengeId);
}
