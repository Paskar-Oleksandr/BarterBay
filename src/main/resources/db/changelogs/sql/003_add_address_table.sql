DROP TABLE IF EXISTS address CASCADE;
DROP SEQUENCE IF EXISTS address_sequence;

CREATE SEQUENCE address_sequence INCREMENT BY 1;

CREATE TABLE address
(
  address_id         BIGINT PRIMARY KEY,
  country            VARCHAR(50) NOT NULL,
  city               VARCHAR(50) NOT NULL,
  street             VARCHAR(50) NOT NULL,
  zip_code           int         NOT NULL,
  created_date       TIMESTAMP   NOT NULL,
  last_modified_date TIMESTAMP   NOT NULL
);

INSERT INTO address
VALUES (1, 'UKR', 'Kharkiv', 'Kalyna', 61000, now(), now());







