package com.wannabe.FinanceTracker.specification;

import com.wannabe.FinanceTracker.model.Transaction;
import com.wannabe.FinanceTracker.model.TransactionDirection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionSpecification {
    public Specification<Transaction> hasCreatedByIn(List<UUID> createdBy) {
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("createdBy")).value(createdBy);
            }
        };
    }

    public Specification<Transaction> hasAmountBetween(BigDecimal minAmount, BigDecimal maxAmount) {
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get("amount"), minAmount, maxAmount);
            }
        };
    }

    public Specification<Transaction> hasCounterPartyIdIn(List<UUID> counterPartyIds) {
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("counterPartyId")).value(counterPartyIds);
            }
        };
    }

    public Specification<Transaction> hasDirectionIn(List<TransactionDirection> directions) {
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("direction")).value(directions);
            }
        };
    }

    public Specification<Transaction> hasTagIn(List<Long> tagIds) {
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("tag").get("id")).value(tagIds);
            }
        };
    }

    public Specification<Transaction> hasMethodIn(List<Long> methodIds) {
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("method").get("id")).value(methodIds);
            }
        };
    }

    public Specification<Transaction> hasCurrencyIn(List<Long> currencyIds) {
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("currency").get("id")).value(currencyIds);
            }
        };
    }
}
