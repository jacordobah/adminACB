INSERT INTO users (document_type, document_number, name, address, role, active)
VALUES ('CC', '1019087452', 'Camilo Pardo', 'Carrera 24 # 12-40', 'GUIDE', 1);
INSERT INTO guides (contract_number, username, password, email, zone, user_id)
VALUES ('CPS-2026-0391', 'cpardo', '$2y$10$0HZhIkxULORjuJEyidsnY.31MatWZSr8MCDRCZBce1lA4oWN1F6N2', 'camilo.pardo@acb.example', 'MM', LAST_INSERT_ID());