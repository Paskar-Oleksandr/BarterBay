DROP TABLE IF EXISTS confirmation_token CASCADE;
DROP SEQUENCE IF EXISTS confirmation_token_sequence;

CREATE SEQUENCE confirmation_token_sequence INCREMENT BY 1;

CREATE TABLE confirmation_token
(
  token_id     BIGINT PRIMARY KEY REFERENCES bb_user (user_id),
  token        VARCHAR(100) NOT NULL,
  created_at   TIMESTAMP    NOT NULL,
  expires_at   TIMESTAMP    NOT NULL,
  confirmed_at TIMESTAMP,
  user_id      BIGINT       NOT NULL
);
