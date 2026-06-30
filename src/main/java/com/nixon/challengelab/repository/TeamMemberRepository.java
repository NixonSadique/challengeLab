package com.nixon.challengelab.repository;

import com.nixon.challengelab.model.TeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findAllByTeamId(Long teamId);

    Page<TeamMember> findAllByUserId(Long userId, Pageable pageable);

    Page<TeamMember> findAllByUserUsername(String userUsername, Pageable pageable);

}
