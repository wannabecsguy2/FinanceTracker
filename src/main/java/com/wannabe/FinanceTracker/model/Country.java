package com.wannabe.FinanceTracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="country")
public class Country {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id", updatable=false, nullable=false)
    private Long id;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="code", nullable=false)
    private String code;

    @Column(name="extension", nullable=false)
    private String extension;
}
