INSERT INTO users (document_type, document_number, name, address, role, active, phone)
VALUES ('CC', '1019087452', 'Camilo Pardo', 'Carrera 24 # 12-40', 'GUIDE', 1, '1234567890');
INSERT INTO guides (contract_number, email, zone, guide_id)
VALUES ('CPS-2026-0391', 'camilo.pardo@acb.example', 'MM', LAST_INSERT_ID());