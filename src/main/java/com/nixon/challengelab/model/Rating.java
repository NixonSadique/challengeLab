package com.nixon.challengelab.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "tb_rating",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"submission_id", "user_id"}
        )
)
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer creativity;

    @Column(nullable = false)
    private Integer technicalQuality;

    @Column(nullable = false)
    private Integer completeness;

    @Column(nullable = false)
    private String feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
