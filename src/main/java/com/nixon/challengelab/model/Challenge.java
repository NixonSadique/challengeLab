package com.nixon.challengelab.model;

import com.nixon.challengelab.model.enums.ChallengeStatus;
import com.nixon.challengelab.model.enums.Difficulty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "tb_challenge")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(nullable = false, length = 100)
    private String category;

    private Integer maxTeamSize;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private ZonedDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "challenge",  cascade = CascadeType.ALL)
    private List<Team> teams;

    @OneToMany(mappedBy = "challenge")
    private List<Submission> submissions;

    public Boolean areTeamsAllowed() {
        return maxTeamSize != null && maxTeamSize > 1;
    }
}

