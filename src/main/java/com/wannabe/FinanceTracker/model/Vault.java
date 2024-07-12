package com.wannabe.FinanceTracker.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name="vault")
public class Vault {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="id", nullable=false, updatable=false, unique=true)
    private UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "user_id", nullable=false, updatable=false, unique = true)
    private UUID userId;

    private boolean active;

    private boolean locked;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="vault", orphanRemoval = true)
    private Set<Document> documents;

    private LocalDateTime created = LocalDateTime.now();
}
