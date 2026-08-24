package com.rcaengine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "git_commits",
        indexes = {
                @Index(name = "idx_commit_hash", columnList = "commitHash"),
                @Index(name = "idx_commit_timestamp", columnList = "committedAt")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class GitCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String repositoryUrl;

    @Column(nullable = false)
    private String commitHash;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    private String author;

    @Column(nullable = false)
    private LocalDateTime committedAt;

    @Column(columnDefinition = "TEXT")
    private String changedFiles;

    @Column(columnDefinition = "TEXT")
    private String diffSummary;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}