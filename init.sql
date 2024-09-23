use FinanceTrackerDev;

-- Add default role
insert into role (id, description, level, name) values
(1, "Super Admin Role", 0, "ROLE_SUPERADMIN"),
(2, "Admin Role", 1, "ROLE_ADMIN"),
(3, "Verified User Role", 6, "ROLE_USER");

-- Add default currency
delete from currency;
insert into currency (id, name, code) values
(1, "Indian Rupee", "INR"),
(2, "United State Dollar", "USD"),
(3, "Great Britain Pound", "GBP"),
(4, "Euro", "EUR"),
(5, "Japanese Yen", "JPY"),
(6, "Chinese Yuan", "CNY");

-- TODO: Add default admin user


-- Add default transaction_tag
insert into transaction_tag (id, name, code, description) values
(1, "Groceries", "GRO", "Transactions associated with Groceries"),
(2, "Miscellaneous", "MISC", "Unclassified Transactions"),
(3, "Restaurants", "RESTAURANTS", ""),
(4, "Transport", "TRANSPORT", "Bus, Taxi, Metro"),
(5, "Travel", "TRAVEL", "Travel related expenses"),
(6, "Entertainment", "ENTERTAINMENT", ""),
(7, "Health", "HEALTH", "Gym, Sports"),
(8, "Services", "SERVICES", ""),
(9, "General", "GENERAL", ""),
(10, "Utilities", "UTILITIES", "Electricity, Water"),
(11, "Cash", "CASH", "Exchanged or Withdrawn"),
(12, "Transfers", "TRANSFERS", ""),
(13, "Insurance", "INSURANCE", ""),
(14, "Wealth", "WEALTH", ""),
(15, "Refund", "REFUND", ""),
(16, "Cashback", "CASHBACK", ""),
(17, "Child Allowance", "ALLOWANCE", ""),
(18, "Investment", "INVESTMENT", ""),
(19, "Loan", "LOAN", ""),
(20, "Credit", "CREDIT", ""),
(21, "Savings", "SAVINGS", ""),
(22, "Donation", "DONATION", ""),
(23, "Salary", "SALARY", ""),
(24, "Gift", "GIFT", ""),
(25, "Top Ups", "TOPUP", ""),
(26, "Net Sales", "NETSALES", ""),
(27, "Interest", "INTEREST", ""),
(28, "Fees and Charges", "FEES", ""),
(29, "Shopping", "SHOPPING", "");

-- TODO: Add default document_type

-- TODO: Add default document_group

-- Add default countries
insert into country (id, name, code, extension, currency_id) values
(1, "India", "IN", "+91", 1),
(2, "Japan", "JP", "+81", 5),
(3, "China", "CH", "+86", 6);

-- Add default transaction methods
insert into transaction_method (id, code, description, name) values
(1, "CASH", "Cash Payment", "CASH"),
(2, "CHEQUE", "Cheque", "Cheque"),
(3, "CREDITCARD", "Credit Card", "Credit Card"),
(4, "DEBITCARD", "Debit Card", "Debit Card");


insert into counter_party (id, name, user_id, type) values
(UUID(), "Default Friend", "8b6b086a-a2e1-4482-9290-7b9c20a2e73e", "FRIEND");

select * from counter_party;
select * from user;

select * from transaction_method;

select * from transaction;
Update transaction set date = CURRENT_TIMESTAMP;
insert into transaction (id, amount, counter_party_id, created, created_by, direction, currency_id, method_id, tag_id, date) values
(UUID(), 210,  "c7bcb86c-6a98-11ef-a74f-58961d3ea54e", CURRENT_TIMESTAMP, "8b6b086a-a2e1-4482-9290-7b9c20a2e73e", "DEBIT", 1, 4, 2, CURRENT_TIMESTAMP);
select * from currency;
select * from transaction_tag;

