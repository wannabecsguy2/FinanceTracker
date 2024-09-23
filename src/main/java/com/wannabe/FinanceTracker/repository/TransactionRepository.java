package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
    Boolean existsByIdAndCreatedBy(UUID id, UUID createdBy);

    @Query("SELECT " +
            "   t.id as id, " +
            "   t.createdBy as createdBy, " +
            "   t.counterParty as counterParty, " +
            "   t.direction as transactionDirection, " +
            "   t.tag as tag, " +
            "   t.method as method, " +
            "   t.amount as amount, " +
            "   t.currency as currency, " +
            "   t.note as note, " +
            "   t.created as created, " +
            "   t.updated as updated " +
            "FROM Transaction t " +
            "WHERE " +
            "   t.counterParty.name LIKE %:keyword% OR " +
            "   t.note LIKE %:keyword% OR " +
            "   t.tag.code LIKE %:keyword% OR " +
            "   t.method.code LIKE %:keyword% ")
    List<Transaction> fetchAllByKeyword(String keyword, Pageable pageable);
}
