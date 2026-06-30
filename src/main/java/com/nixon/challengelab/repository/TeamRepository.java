package com.nixon.challengelab.repository;

import com.nixon.challengelab.model.Challenge;
import com.nixon.challengelab.model.Team;
import com.nixon.challengelab.model.TeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface TeamRepository extends JpaRepository<Team, Long> {
    Page<Team> findAllByChallenge(Challenge challenge, Pageable pageable);

    Optional<Team> findByMembersContaining(TeamMember members);


}
