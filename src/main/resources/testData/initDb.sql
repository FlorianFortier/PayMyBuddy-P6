INSERT INTO user (balance, enabled, bank_id, user_id, created_at, email, first_name, last_name, pwd, username) VALUES (8910.45, true, null, 1, null, 'florian.f.imp@gmail.com', 'Florian', 'Fortier', '$2a$10$w41K6EWkwiUYYK3w2ZlRyO49FfqSlgMVHWSy/9dKotsVd7zPhPrw6', null);
INSERT INTO user (balance, enabled, bank_id, user_id, created_at, email, first_name, last_name, pwd, username) VALUES (189.55, true, null, 2, null, 'tst@gmail.com', 'COntactTest', 'Fortier', '$2a$10$w41K6EWkwiUYYK3w2ZlRyO49FfqSlgMVHWSy/9dKotsVd7zPhPrw6', 'Contact');
INSERT INTO user (balance, enabled, bank_id, user_id, created_at, email, first_name, last_name, pwd, username) VALUES (189.55, true, null, 3, null, 'tstContact@gmail.com', 'TestAddContact', 'Fortier', '$2a$10$w41K6EWkwiUYYK3w2ZlRyO49FfqSlgMVHWSy/9dKotsVd7zPhPrw6', 'Contact');

INSERT INTO bank (balance, id, address, name) VALUES (1000, 1, '51 St liverpoool', 'Bank1');
INSERT INTO bank (balance, id, address, name) VALUES (1000, 2, '51 wall street', 'Bank2');

INSERT INTO user_role (user_id, role) VALUES (1, 'USER');

INSERT INTO contact (contact_id, id, user_id) VALUES (1, 1, 2);
INSERT INTO contact (contact_id, id, user_id) VALUES (2, 2, 1);

INSERT INTO transaction (amount, created_at, emitter_bank_id, emitter_user_id, id, receiver_bank_id, receiver_user_id, description) VALUES (21, '2024-01-12 14:24:51.000000', 2, 2, 1, 1, 1, 'Test for transaction');
INSERT INTO verif_token (expiry_date, id, user_id, token) VALUES ('2024-01-12 14:38:29.117000', 1, 1, 'ab0b3974-0a9e-44f4-abe3-89c4a8a04746');
INSERT INTO verif_token_seq (next_val) VALUES (51);