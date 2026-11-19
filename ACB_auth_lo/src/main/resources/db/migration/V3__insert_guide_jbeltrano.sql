-- Usuario guía de pruebas. Conserva cpardo (guide_id 1) como MANAGER.
-- Usa un id alto para no chocar con datos de prueba ya existentes.
-- Password: guia2026
INSERT INTO guides_login (guide_id, username, password, role, active)
VALUES (9001, 'jbeltrano',
        '$2a$10$Sp4nwtUzpN4dnKtn7G2c.OxQgWyXmU4F.8D0EIh89lz../a7UqcYu',
        'GUIDE',
        1);
