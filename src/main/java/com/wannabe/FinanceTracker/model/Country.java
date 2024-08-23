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

    @OneToOne(fetch=FetchType.EAGER, optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="currency_id", referencedColumnName="id", nullable=false, updatable=false)
    private Currency currency;
}
