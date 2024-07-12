package com.wannabe.FinanceTracker.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name="otp")
public class OTP {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id", nullable=false, updatable=false, unique=true)
    private Long id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name="otp", nullable = false)
    private String otp;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable = false)
    private OTPType type;

    @Column(name="verified", nullable = false)
    private boolean verified = false;

    private LocalDateTime created = LocalDateTime.now();

    @Column(name="expires", nullable = false)
    private LocalDateTime expires;

    private LocalDateTime updated;
}
