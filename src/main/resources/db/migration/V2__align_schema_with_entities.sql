   ALTER TABLE guides RENAME COLUMN `zone覆` TO zone;
   ALTER TABLE users MODIFY COLUMN document_type ENUM('CC','CE','NUIP','PPT','PPE') NOT NULL;