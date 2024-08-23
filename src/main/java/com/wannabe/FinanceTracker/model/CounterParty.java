package com.wannabe.FinanceTracker.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@Table(name="counter_party")
public class CounterParty {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="id", nullable=false, updatable=false, unique=true)
    private UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "user_id", nullable = false, updatable=false)
    private UUID userId;

    @Column(name="name", nullable=false)
    private String name;

    private String email;

    private String phone;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "associated_user_id")
    private UUID associatedUserId;
}
