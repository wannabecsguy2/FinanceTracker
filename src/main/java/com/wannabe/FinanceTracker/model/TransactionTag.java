package com.wannabe.FinanceTracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="transaction_tag")
public class TransactionTag {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", updatable=false, nullable=false)
    private Long id;

    private String name;

    @Column(name="code", nullable=false, unique=true)
    private String code;

    private String description;
}
