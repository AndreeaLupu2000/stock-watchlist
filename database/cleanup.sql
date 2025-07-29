-- cleanup.sql

-- Connect to stock_db database
\c stock_db;

-- Drop tables (just in case)
DROP TABLE IF EXISTS stock.watchlist_items;
DROP TABLE IF EXISTS stock.stocks;
DROP TABLE IF EXISTS stock.users;

-- Drop schema
DROP SCHEMA IF EXISTS stock CASCADE;

-- Connect to a different database before dropping stock_db
\c postgres;

-- Drop stock_db database
DROP DATABASE IF EXISTS stock_db;

-- Drop user
DROP USER IF EXISTS stock_user;