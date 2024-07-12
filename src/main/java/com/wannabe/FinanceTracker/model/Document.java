package com.wannabe.FinanceTracker.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name="document")
public class Document {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="id", nullable=false, updatable=false, unique=true)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="vault_id", referencedColumnName = "id", nullable = false)
    private Vault vault;

    @OneToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="type_id", referencedColumnName="id", nullable=false)
    private DocumentType type;

    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name="join_document_document_group",
            joinColumns=@JoinColumn(name = "document_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name = "document_group_id", referencedColumnName = "id"))
    private Set<DocumentGroup> groups;

    @Column(name="path", nullable = false, updatable = false)
    private String path;
}
