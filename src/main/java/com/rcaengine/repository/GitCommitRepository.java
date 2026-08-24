package com.rcaengine.repository;

import com.rcaengine.entity.GitCommit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GitCommitRepository extends JpaRepository<GitCommit, Long> {

    Optional<GitCommit> findByRepositoryUrlAndCommitHash(
            String repositoryUrl,
            String commitHash
    );

    List<GitCommit> findByRepositoryUrlAndCommittedAtBetween(
            String repositoryUrl,
            LocalDateTime start,
            LocalDateTime end
    );
}