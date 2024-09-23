package com.wannabe.FinanceTracker.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="id", nullable=false, updatable=false, unique=true)
    private UUID id;

    @Column(name="email", unique=true)
    private String email;

    @Column(name="username", unique=true)
    private String username;

    @Column(name="phone", nullable=false, unique=true)
    private String phone;

    @JsonIgnore
    @Column(name="password")
    private String password;

    @Column(name="active", nullable=false)
    private boolean active = true;

    @Column(name="email_verified", nullable=false)
    private boolean emailVerified = false;

    @Column(name="phone_verified", nullable=false)
    private boolean phoneVerified = false;

    @OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="user_profile_id", referencedColumnName="id")
    private UserProfile userProfile;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime updated;

    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name="join_user_role",
            joinColumns=@JoinColumn(name = "user_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    @OneToOne(fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name="country_id", referencedColumnName="id", nullable = false)
    private Country defaultCountry;

    @Column(name="registration_step", nullable=false)
    @Enumerated(EnumType.STRING)
    private RegistrationStep registrationStep = RegistrationStep.PHONE_REGISTERED;
}
