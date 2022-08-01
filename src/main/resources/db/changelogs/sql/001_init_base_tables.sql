DROP TABLE IF EXISTS bb_user CASCADE;
DROP SEQUENCE IF EXISTS user_sequence;

CREATE SEQUENCE user_sequence INCREMENT BY 1;

CREATE TABLE bb_user
(
    user_id            BIGINT PRIMARY KEY,
    email              VARCHAR(100) UNIQUE NOT NULL,
    first_name         VARCHAR(100)        NOT NULL,
    last_name          VARCHAR(100)        NOT NULL,
    password           VARCHAR(100)        NOT NULL,
    version            BIGINT,
    created_date       TIMESTAMP           NOT NULL,
    last_modified_date TIMESTAMP           NOT NULL
);

CREATE UNIQUE INDEX ue_index ON bb_user (email);

