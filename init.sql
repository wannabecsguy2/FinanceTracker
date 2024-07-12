use FinanceTrackerDev;

-- Add default role
insert into role (id, description, level, name) values
(1, "Super Admin Role", 0, "ROLE_SUPERADMIN"),
(2, "Admin Role", 1, "ROLE_ADMIN"),
(3, "User Role", 6, "ROLE_USER");

-- Add default currency
insert into currency (id, name, code) values
(1, "Indian Rupee", "INR"),
(2, "United State Dollar", "USD"),
(3, "Great Britain Pound", "GBP"),
(4, "Euro", "EUR"),
(5, "Japanese Yen", "JPY");

-- TODO: Add default admin user


-- Add default transaction_type
insert into transaction_type (id, name, code, description) values
(1, "Groceries", "GRO", "Transactions associated with Groceries"),
(2, "Miscellaneous", "MISC", "Unclassified Transactions");

-- TODO: Add default document_type

-- TODO: Add default document_group

