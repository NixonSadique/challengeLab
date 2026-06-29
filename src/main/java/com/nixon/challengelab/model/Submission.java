package com.nixon.challengelab.model;

import com.nixon.challengelab.model.enums.SubmissionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tb_submission", check = @CheckConstraint(
        constraint = "(team_id IS NULL) <> (user_id IS NULL)"
))
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String solutionUrl;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    private List<Rating> ratings;





}
