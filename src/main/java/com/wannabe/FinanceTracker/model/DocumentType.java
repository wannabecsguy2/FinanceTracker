package com.wannabe.FinanceTracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="document_type")
public class DocumentType {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id", updatable=false, nullable=false)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="code", nullable = false, unique = true)
    private String code;
}
