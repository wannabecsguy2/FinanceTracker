package com.wannabe.FinanceTracker.model;

import com.wannabe.FinanceTracker.payload.AddTransactionRequest;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name="transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="id", nullable=false, updatable=false, unique=true)
    private UUID id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="created_by", nullable = false, updatable = false)
    private UUID createdBy;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name="counter_party_id", nullable = false)
    private UUID counterPartyId;

    @Enumerated(EnumType.STRING)
    private TransactionDirection direction;

    @OneToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="tag_id", referencedColumnName="id", nullable=false)
    private TransactionTag tag;

    @OneToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="method_id", referencedColumnName="id", nullable=false)
    private TransactionMethod method;

    @Column(name="amount", nullable = false)
    private BigDecimal amount;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "currency_id", referencedColumnName = "id", nullable = false)
    private Currency currency;

    private String note;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime updated;

    // TODO: Add receipts in Transaction
}
