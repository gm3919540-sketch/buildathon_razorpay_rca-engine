package com.rcaengine.repository;

import com.rcaengine.entity.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GitRepositoryRepository extends JpaRepository<GitRepository, Long> {

    Optional<GitRepository> findByUrl(String url);
}