DELETE FROM users_properties;
DELETE FROM usergroups_properties;
DELETE FROM users_usergroups;
DELETE FROM user_groups;
DELETE FROM users;
INSERT INTO user_groups (name, display_name, creation_date, modification_date, role) VALUES
  ('admins', 'Administrators', NOW(), NOW(), 'ADMINISTRATOR'),
  ('users', 'Users', NOW(), NOW(), 'USER');
INSERT INTO usergroups_properties (usergroup_id, name, data, data_type, data_type_def, label, default_value) VALUES
  ((SELECT id FROM user_groups WHERE name = 'admins'), 'prop10', 'val10', 'STRING', NULL, 'Property 10', NULL),
  ((SELECT id FROM user_groups WHERE name = 'admins'), 'prop11', 'val11', 'STRING', NULL, 'Property 11', NULL),
  ((SELECT id FROM user_groups WHERE name = 'users'), 'prop20', 'val20', 'STRING', NULL, 'Property 20', NULL),
  ((SELECT id FROM user_groups WHERE name = 'users'), 'prop30', 'val30', 'STRING', NULL, 'Property 30', NULL),
  ((SELECT id FROM user_groups WHERE name = 'users'), 'prop40', 'val40', 'STRING', NULL, 'Property 40', NULL);