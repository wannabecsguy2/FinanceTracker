package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.*;
import com.wannabe.FinanceTracker.payload.*;
import com.wannabe.FinanceTracker.repository.*;
import com.wannabe.FinanceTracker.security.UserPrincipal;
import com.wannabe.FinanceTracker.specification.TransactionSpecification;
import com.wannabe.FinanceTracker.utils.CommonFunctionsUtils;
import com.wannabe.FinanceTracker.utils.RoleAuthorizationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TransactionService {
    @Autowired
    private RoleAuthorizationUtils roleAuthorizationUtils;

    @Autowired
    private CommonFunctionsUtils commonFunctionsUtils;

    @Autowired
    private TransactionSpecification transactionSpecification;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CounterPartyRepository counterPartyRepository;

    @Autowired
    private TransactionTagRepository transactionTagRepository;

    @Autowired
    private TransactionMethodRepository transactionMethodRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Transactional
    public GenericResponseObject<?> add(UserPrincipal userPrincipal, AddTransactionRequest addTransactionRequest) throws Exception {
        if (roleAuthorizationUtils.isUserAuthorized(userPrincipal, "ROLE_ADMIN")) {
            addTransactionRequest.setCreatedBy(userPrincipal.getId());
        }

        Transaction newTransaction = new Transaction();

        // Fetch Tag, Method, Currency, CounterParty
        TransactionTag tag = null;
        TransactionMethod method = null;
        Currency currency = null;
        CounterParty counterParty = null;

        if (!counterPartyRepository.existsById(addTransactionRequest.getCounterPartyId())) {
            throw new ResourceNotFoundException("Counter Party not found");
        }
        if (addTransactionRequest.getTagId() != null) {
            tag = transactionTagRepository.findById(addTransactionRequest.getTagId()).orElseThrow(() -> new ResourceNotFoundException("Specified Tag not found"));
        }

        if (addTransactionRequest.getMethodId() != null) {
            method = transactionMethodRepository.findById(addTransactionRequest.getMethodId()).orElseThrow(() -> new ResourceNotFoundException("Specified Method not found"));
        }

        if (addTransactionRequest.getCurrencyId() != null) {
            currency = currencyRepository.findById(addTransactionRequest.getCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Specified Currency not found"));
        }

        if (addTransactionRequest.getCounterPartyId() != null) {
            counterParty = counterPartyRepository.findByUserIdAndId(userPrincipal.getId(), addTransactionRequest.getCounterPartyId()).orElseThrow(() -> new ResourceNotFoundException("Specified CounterParty not found"));
        }

        commonFunctionsUtils.mapTransaction(newTransaction, addTransactionRequest, currency, tag, method, counterParty);

        // TODO: Send notification to CounterParty if in internal environment
        try {
            transactionRepository.save(newTransaction);
            return new GenericResponseObject<>(true, "Transaction added successfully");
        } catch (Exception e) {
            log.error("Exception occurred while saving new transaction", e);
            throw new Exception("Exception occurred while adding transaction");
        }
    }

    @Transactional
    public GenericResponseObject<?> update(UserPrincipal userPrincipal, UpdateTransactionRequest updateTransactionRequest) throws Exception {
        // Fetch Transaction
        Transaction existingTransaction = transactionRepository.findById(updateTransactionRequest.getId()).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        // Check if user is authorized to update transaction
        if (!roleAuthorizationUtils.isUserAuthorized(userPrincipal, "ROLE_ADMIN")) {
            if (userPrincipal.getId().compareTo(existingTransaction.getCreatedBy()) != 0) {
                return new GenericResponseObject<>(false, "You are not authorized to update this transaction");
            }
        }

        TransactionTag tag = null;
        TransactionMethod method = null;
        Currency currency = null;
        CounterParty counterParty = null;

        if (!counterPartyRepository.existsById(updateTransactionRequest.getCounterPartyId())) {
            throw new ResourceNotFoundException("Counter Party not found");
        }
        if (updateTransactionRequest.getTagId() != null) {
            tag = transactionTagRepository.findById(updateTransactionRequest.getTagId()).orElseThrow(() -> new ResourceNotFoundException("Specified Tag not found"));
        }

        if (updateTransactionRequest.getMethodId() != null) {
            method = transactionMethodRepository.findById(updateTransactionRequest.getMethodId()).orElseThrow(() -> new ResourceNotFoundException("Specified Method not found"));
        }

        if (updateTransactionRequest.getCurrencyId() != null) {
            currency = currencyRepository.findById(updateTransactionRequest.getCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Specified Currency not found"));
        }

        if (updateTransactionRequest.getCounterPartyId() != null) {
            counterParty = counterPartyRepository.findById(updateTransactionRequest.getCounterPartyId()).orElseThrow(() -> new ResourceNotFoundException("Specified CounterParty not found"));
        }

        commonFunctionsUtils.mapTransaction(existingTransaction, updateTransactionRequest, currency, tag, method, counterParty);

        // TODO: Send Update Transaction Notification to CounterParty if in internal environment
        try {
            transactionRepository.save(existingTransaction);
            return new GenericResponseObject<>(true, "Transaction updated successfully");
        } catch (Exception e) {
            log.error("Exception occurred while updating transaction", e);
            throw new Exception("Exception occurred while updating transaction");
        }
    }

    @Transactional
    public GenericResponseObject<?> delete(UserPrincipal userPrincipal, UUID transactionId) throws Exception {
        // Fetch Transaction
        Transaction existingTransaction = transactionRepository.findById(transactionId).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        // Check if user is authorized to update transaction
        if (!roleAuthorizationUtils.isUserAuthorized(userPrincipal, "ROLE_ADMIN")) {
            if (userPrincipal.getId().compareTo(existingTransaction.getCreatedBy()) != 0) {
                return new GenericResponseObject<>(false, "You are not authorized to update this transaction");
            }
        }

        // TODO: Send Delete Transaction Notification to CounterParty if in internal environment
        try {
            transactionRepository.delete(existingTransaction);
            return new GenericResponseObject<>(true, "Transaction deleted successfully");
        } catch (Exception e) {
            log.error("Exception occurred while deleting transaction", e);
            throw new Exception("Exception occurred while deleting transaction");
        }
    }

    @Transactional
    public GenericResponseObject<?> batchDelete(UserPrincipal userPrincipal, List<UUID> transactionIds) throws Exception {
        if (!roleAuthorizationUtils.isUserAuthorized(userPrincipal, "ROLE_ADMIN")) {
            for (UUID transactionId: transactionIds) {
                if (!transactionRepository.existsByIdAndCreatedBy(transactionId, userPrincipal.getId())) {
                    throw new ResourceNotFoundException("Transaction does not exists or was not created by user");
                }
            }
        }

        try {
            transactionRepository.deleteAllById(transactionIds);
            return new GenericResponseObject<>(true, "Transactions deleted successfully");
        } catch (Exception e) {
            log.error("Exception occurred while deleting transactions", e);
            throw new Exception("Exception occurred while deleting transactions");
        }
    }

    public GenericResponseObject<?> fetch(UserPrincipal userPrincipal, TransactionFetchFilter filter, Integer pageNumber, Integer pageSize) throws Exception {
        if (!roleAuthorizationUtils.isUserAuthorized(userPrincipal, "ROLE_ADMIN")) {
            filter.setCreatedBy(List.of(userPrincipal.getId()));
        }

        Sort sort = getFilterSort(filter);
        Pageable page = PageRequest.of(pageNumber, pageSize, sort);

        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            filter.setKeyword(filter.getKeyword().toLowerCase());

            return new GenericResponseObject<>(true, "Returning filtered transactions", transactionRepository.fetchAllByKeyword(filter.getKeyword(), page));

        } else{
            Specification<Transaction> filterSpecification = getFilterSpecification(filter);

            if (filterSpecification == null) {
                return new GenericResponseObject<>(true, "Returning filtered transactions", transactionRepository.findAll(page));
            } else {
                return new GenericResponseObject<>(true, "Returning filtered transactions", transactionRepository.findAll(filterSpecification, page));
            }
        }
    }

    private Specification<Transaction> getFilterSpecification(TransactionFetchFilter filter) {
        List<Specification<Transaction>> validSpecifications = new ArrayList<>();
        if (!filter.getCounterPartyIds().isEmpty()) {
            validSpecifications.add(transactionSpecification.hasCounterPartyIdIn(filter.getCounterPartyIds()));
        }
        if (!filter.getCreatedBy().isEmpty()) {
            validSpecifications.add(transactionSpecification.hasCreatedByIn(filter.getCreatedBy()));
        }

        if (filter.getMaxAmount() != null) {
            validSpecifications.add(transactionSpecification.hasAmountBetween(filter.getMinAmount(), filter.getMaxAmount()));
        }

        if (!filter.getDirections().isEmpty()) {
            validSpecifications.add(transactionSpecification.hasDirectionIn(filter.getDirections()));
        }

        if (!filter.getTagIds().isEmpty()) {
            validSpecifications.add(transactionSpecification.hasTagIn(filter.getTagIds()));
        }

        if (!filter.getMethodIds().isEmpty()) {
            validSpecifications.add(transactionSpecification.hasMethodIn(filter.getMethodIds()));
        }

        if (!filter.getCurrencyIds().isEmpty()) {
            validSpecifications.add(transactionSpecification.hasCurrencyIn(filter.getCurrencyIds()));
        }

        if (validSpecifications.isEmpty()) {
            return null;
        }
        return Specification.allOf(validSpecifications);
    }

    private Sort getFilterSort(TransactionFetchFilter filter) {
        if (filter.getSortBy() == null) {
            return Sort.by("date").descending();
        }
        return Sort.by(filter.getSortBy());
    }
}
