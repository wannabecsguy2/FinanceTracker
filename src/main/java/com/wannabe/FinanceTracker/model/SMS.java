package com.wannabe.FinanceTracker.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name="sms")
public class SMS {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="id", nullable=false, updatable=false, unique=true)
    private UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="user_id", nullable=false, updatable=false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false)
    private SMSStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable=false, updatable=false)
    private SMSType type;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime updated;

    @Column(name="sid", nullable=false, updatable=false)
    private String sid;
}
