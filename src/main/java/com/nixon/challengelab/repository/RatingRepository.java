package com.nixon.challengelab.repository;

import com.nixon.challengelab.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Page<Rating> findAllBySubmissionId(Long submissionId, Pageable pageable);

    Boolean existsBySubmissionIdAndUserId(Long submissionId, Long userId);

    @Query("""
        SELECT AVG( (r.completeness + r.creativity + r.technicalQuality)/3 )
        FROM Rating r
        WHERE r.submission.id = :submissionId
    """)
    Double getAverageRatingBySubmissionId(Long submissionId);
}
