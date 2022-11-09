ALTER TABLE bb_user
  ALTER COLUMN first_name
    DROP NOT NULL;

ALTER TABLE bb_user
  ALTER COLUMN last_name
    DROP NOT NULL;
