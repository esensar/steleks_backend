INSERT INTO user_role(role_name) VALUES ('USER'), ('ADMIN');
INSERT INTO user(first_name, last_name, username, contact_number, card_number, registration_date, email, password_hash)
VALUES ('Dino', 'Dizdarevic', 'steleks_admin', '123-456-789', 0, CAST('1970-01-01 00:00:00' as datetime),'admin@steleks.ba', '$2a$10$DLPXY.SGK/foA1h4p2kqY.CDpVBQTVQEh9fdsHwbg5v/5Gcaws6VS');
INSERT INTO user_user_roles(user_id, user_roles_id) VALUES (1, 1), (1, 2)
# comein123