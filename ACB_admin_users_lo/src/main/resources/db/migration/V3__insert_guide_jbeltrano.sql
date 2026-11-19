-- Guía de pruebas con el mismo guide_id alto que ACB_auth_lo (jbeltrano).
INSERT INTO users (user_id, document_type, document_number, name, address, role, active, phone)
VALUES (9001, 'CC', '1099000001', 'J. Beltrano', 'Carrera 24 # 12-40', 'GUIDE', 1, '1234567891');

INSERT INTO guides (contract_number, email, zone, guide_id)
VALUES ('CPS-2026-0392', 'jbeltrano@acb.example', 'MM', 9001);
