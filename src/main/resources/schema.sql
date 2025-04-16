DROP TABLE IF EXISTS csvtodbdata;

CREATE TABLE csvtodbdata (
    id BIGINT PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    email VARCHAR(100)
);
