package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
}
