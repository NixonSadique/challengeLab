package com.nixon.challengelab.repository;

import com.nixon.challengelab.dto.request.ChallengeFilters;
import com.nixon.challengelab.model.Challenge;
import com.nixon.challengelab.model.enums.ChallengeStatus;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long>, JpaSpecificationExecutor<Challenge> {

    List<Challenge> findAllByCreatorId(Long creatorId);

    Page<Challenge> findAllByCreatorId(Long creatorId, Pageable pageable);

    Page<Challenge> findAllByCreatorEmailOrCreatorUsername(String email, String username,Pageable pageable);

    @Transactional
    @Modifying
    @Query("""
           UPDATE Challenge c SET
           c.status = :status
           WHERE c.deadline < CURRENT_TIMESTAMP
          """)
    int updateChallengeStatusToClosed(ChallengeStatus status);

    static Specification<Challenge> withFilters(ChallengeFilters filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.title() != null && !filters.title().isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")), "%" + filters.title() + "%"
                        )
                );
            }

            if (filters.deadline() != null) {
                predicates.add(
                        criteriaBuilder.lessThan(root.get("deadline"), filters.deadline())
                );
            }

            if (filters.difficulty() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("difficulty"), filters.difficulty())
                );
            }

            if (filters.maxAllowedTeamSize() != null && filters.maxAllowedTeamSize() > 0) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("maxTeamSize"), filters.maxAllowedTeamSize())
                );
            }

            if (filters.status() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("status"), filters.status())
                );
            }


            return criteriaBuilder.and(predicates);
        };
    }

    Long countByDeadlineAfter(ZonedDateTime deadlineAfter);
}
