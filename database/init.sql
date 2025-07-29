-- Create user
CREATE USER stock_user WITH PASSWORD 'stock_pass';

-- Create database  
CREATE DATABASE stock_db OWNER stock_user;

-- Connect to stock_db database
\c stock_db

-- Create schema in the stock_db database
CREATE SCHEMA IF NOT EXISTS stock AUTHORIZATION stock_user;

-- Set search path
SET search_path TO stock;

-- Grant privileges
GRANT ALL PRIVILEGES ON SCHEMA stock TO stock_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA stock GRANT ALL ON TABLES TO stock_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA stock GRANT ALL ON SEQUENCES TO stock_user;

-- Create tables
CREATE TABLE IF NOT EXISTS stocks (
    symbol VARCHAR(10) PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(255) PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS watchlist_items (
    id BIGSERIAL PRIMARY KEY,
    stock_symbol VARCHAR(10) NOT NULL REFERENCES stocks(symbol) ON DELETE RESTRICT,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(255) NOT NULL REFERENCES users(username) ON DELETE RESTRICT
);

-- Insert mock data
INSERT INTO stocks (symbol, company_name, price) VALUES
('AAPL', 'Apple Inc.', 150.25),
('AMZN', 'Amazon.com Inc.', 3250.78),
('TSLA', 'Tesla Inc.', 785.42),
('MSFT', 'Microsoft Corporation', 265.88),
('GOOG', 'Alphabet Inc. (Class C)', 2487.12),
('BAC', 'Bank of America Corporation', 42.39),
('META', 'Meta Platforms Inc.', 182.54),
('JNJ', 'Johnson & Johnson', 168.73),
('BRK.B', 'Berkshire Hathaway Inc. (Class B)', 330800.00),
('NFLX', 'Netflix Inc.', 201.11);

