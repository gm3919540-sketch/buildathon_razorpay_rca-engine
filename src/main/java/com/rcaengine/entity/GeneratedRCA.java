package com.rcaengine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "generated_rcas")
@Getter
@Setter
@NoArgsConstructor
public class GeneratedRCA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false, unique = true)
    private Incident incident;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String rootCause;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String evidence;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String recommendedActions;

    @Column(nullable = false)
    private String confidence;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    @Column(nullable = false)
    private boolean reviewed = false;

    @Column(nullable = false)
    private boolean indexed = false;

    @Column(columnDefinition = "TEXT")
    private String actualRootCause;

    @Column(columnDefinition = "TEXT")
    private String actualResolution;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
}