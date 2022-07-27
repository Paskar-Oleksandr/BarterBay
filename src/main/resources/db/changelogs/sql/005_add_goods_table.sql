ALTER TABLE bb_user
  ADD COLUMN good BIGINT;

DROP TABLE IF EXISTS good CASCADE;
DROP SEQUENCE IF EXISTS good_sequence;

CREATE SEQUENCE good_sequence INCREMENT BY 1;

CREATE TABLE good
(
  good_id            BIGINT PRIMARY KEY,
  name               VARCHAR(100) NOT NULL,
  description        VARCHAR(200) NOT NULL,
  category           VARCHAR      NOT NULL,
  address_id         BIGINT       NOT NULL,
  user_id            BIGINT       NOT NULL,
  created_date       TIMESTAMP    NOT NULL,
  last_modified_date TIMESTAMP    NOT NULL,
  FOREIGN KEY (address_id) REFERENCES address (address_id),
  FOREIGN KEY (user_id) REFERENCES bb_user (user_id)
);

ALTER TABLE address
  ADD COLUMN good BIGINT;
ALTER TABLE address
  ADD FOREIGN KEY (good) REFERENCES good (good_id);

INSERT INTO good
VALUES (1, 'Tomatoes', ' Fresh and testy', 'FOOD', 1, 1, now(), now());

