DELETE FROM users_properties;
DELETE FROM usergroups_properties;
DELETE FROM users_usergroups;
DELETE FROM user_groups;
DELETE FROM users;
INSERT INTO user_groups (name, display_name, creation_date, modification_date, role) VALUES
  ('admins', 'Administrators', NOW(), NOW(), 'ADMINISTRATOR'),
  ('users', 'Users', NOW(), NOW(), 'USER');
INSERT INTO users (name, display_name, creation_date, modification_date, connections_count, email, expiration_date, connection_date, ip, locked, password, role) VALUES
  ('admin', 'Administrator', NOW(), NOW(), 0, 'admin@company.com', NULL, NULL, NULL, false, '21232F297A57A5A743894A0E4A801FC3', 'ADMINISTRATOR'),
  ('anonymous', 'Anonymous', NOW(), NOW(), 0, NULL, NULL, NULL, NULL, false, '', 'ANONYMOUS'),
  ('user1', 'User 1', NOW(), NOW(), 2, 'user1@company.com', NULL, NOW(), '192.168.0.101', false, '24C9E15E52AFC47C225B757E7BEE1F9D', 'USER'),
  ('user2', 'User 2', NOW(), NOW(), 2, 'user2@company.com', NULL, NOW(), '192.168.0.102', false, '24C9E15E52AFC47C225B757E7BEE1F9D', 'USER'),
  ('user3', 'User 3', NOW(), NOW(), 2, 'user3@company.com', NULL, NOW(), '192.168.0.103', false, '24C9E15E52AFC47C225B757E7BEE1F9D', 'USER');
INSERT INTO users_usergroups (user_id, usergroup_id) VALUES
  ((SELECT id FROM users WHERE name = 'admin'), (SELECT id FROM user_groups WHERE name = 'admins')),
  ((SELECT id FROM users WHERE name = 'user1'), (SELECT id FROM user_groups WHERE name = 'users')),
  ((SELECT id FROM users WHERE name = 'user2'), (SELECT id FROM user_groups WHERE name = 'users')),
  ((SELECT id FROM users WHERE name = 'user3'), (SELECT id FROM user_groups WHERE name = 'users'));
INSERT INTO users_properties (user_id, name, data, data_type, data_type_def, label, default_value) VALUES
  ((SELECT id FROM users WHERE name = 'admin'), 'prop10', 'val10', 'STRING', NULL, 'Property 10', NULL),
  ((SELECT id FROM users WHERE name = 'admin'), 'prop11', 'val11', 'STRING', NULL, 'Property 11', NULL),
  ((SELECT id FROM users WHERE name = 'user1'), 'prop20', 'val20', 'STRING', NULL, 'Property 20', NULL),
  ((SELECT id FROM users WHERE name = 'user2'), 'prop30', 'val30', 'STRING', NULL, 'Property 30', NULL),
  ((SELECT id FROM users WHERE name = 'user3'), 'prop40', 'val40', 'STRING', NULL, 'Property 40', NULL);