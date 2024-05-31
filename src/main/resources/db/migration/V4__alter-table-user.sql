ALTER TABLE users
ADD CONSTRAINT unique_nickname UNIQUE (nickname);

ALTER TABLE users
ADD CONSTRAINT unique_email UNIQUE (email);

ALTER TABLE users
ADD CONSTRAINT unique_password UNIQUE (`password`);
