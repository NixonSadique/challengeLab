package com.nixon.challengelab.repository;

import com.nixon.challengelab.model.TeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    Page<TeamMember> findAllByTeamId(Long teamId, Pageable pageable);

    Page<TeamMember> findAllByUserId(Long userId, Pageable pageable);

    Page<TeamMember> findAllByUserUsername(String userUsername, Pageable pageable);

    Optional<TeamMember> findByTeamIdAndUserId(Long teamId, Long userId);

    boolean existsByTeamIdAndUserId(Long teamId, Long userId);

    int countByTeamId(Long teamId);

}
