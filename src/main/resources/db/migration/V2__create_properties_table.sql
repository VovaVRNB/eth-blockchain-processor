CREATE TABLE properties (
    id SERIAL PRIMARY KEY,
    key varchar(256) NOT NULL,
    value varchar(256) NOT NULL
);

INSERT INTO properties(id, key, value) VALUES(1, 'LAST_BLOCK_ID', '')