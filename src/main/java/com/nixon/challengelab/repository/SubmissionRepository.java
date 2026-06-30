package com.nixon.challengelab.repository;

import com.nixon.challengelab.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findAllByChallengeId(Long challengeId);

    List<Submission> findAllByTeamId(Long teamId);

    Boolean existsByTeamIdAndChallengeId(Long teamId, Long challengeId);
}
