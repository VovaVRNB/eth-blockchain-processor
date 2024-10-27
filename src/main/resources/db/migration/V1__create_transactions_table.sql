CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    hash VARCHAR(66) NOT NULL,
    block_hash VARCHAR(66) NOT NULL,
    block_number BIGINT NOT NULL,
    from_address VARCHAR(42) NOT NULL,
    to_address VARCHAR(42),
    value DECIMAL(30, 0) NOT NULL,
    gas DECIMAL(30, 0) NOT NULL,
    gas_price DECIMAL(30, 0) NOT NULL,
    nonce BIGINT,
    input TEXT,
    transaction_index BIGINT NOT NULL,
    creates VARCHAR(42),
    raw TEXT,
    r VARCHAR(66),
    s VARCHAR(66),
    v BIGINT
)