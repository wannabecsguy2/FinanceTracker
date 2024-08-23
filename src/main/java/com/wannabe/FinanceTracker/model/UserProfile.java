package com.wannabe.FinanceTracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="id", nullable=false, updatable = false)
    private UUID id;

    @Column(name="first_name", nullable=false)
    private String firstName;

    @Column(name="last_name", nullable=false)
    private String lastName;

    @Column(name="birth_date")
    @Temporal(value= TemporalType.DATE)
    private Date birthDate;
}
