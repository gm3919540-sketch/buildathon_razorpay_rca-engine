package com.rcaengine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "repositories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_repository_url",
                        columnNames = "url"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class GitRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private String localPath;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}