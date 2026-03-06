ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS password VARCHAR(255);

UPDATE empleados
SET password = '{noop}cambia-esta-clave'
WHERE password IS NULL OR btrim(password) = '';

ALTER TABLE empleados
    ALTER COLUMN password SET NOT NULL;
