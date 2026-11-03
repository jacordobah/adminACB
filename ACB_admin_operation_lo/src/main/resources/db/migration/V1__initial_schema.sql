-- Operacion - PostgreSQL
-- guide_id, student_id, route_id y shift_code viven en otras bases de datos,
-- por eso son columnas indexadas y no llaves foraneas.

-- Codigos de radio de la operacion
CREATE TABLE IF NOT EXISTS action_codes (
    code_id     INTEGER      NOT NULL,
    description VARCHAR(150) NOT NULL,
    PRIMARY KEY (code_id)
);

-- Un recorrido de una ruta en una fecha
CREATE TABLE IF NOT EXISTS trips (
    trip_id    BIGINT      GENERATED ALWAYS AS IDENTITY,
    trip_code  VARCHAR(3)  NOT NULL,
    start_time TIMESTAMPTZ NOT NULL,
    end_time   TIMESTAMPTZ NULL,
    route_id   VARCHAR(4)  NOT NULL,
    shift_code VARCHAR(3)  NOT NULL,
    PRIMARY KEY (trip_id),
    CONSTRAINT ck_trips_trip_code  CHECK (trip_code  IN ('541','542')),
    CONSTRAINT ck_trips_shift_code CHECK (shift_code IN ('541','542'))
);
CREATE INDEX IF NOT EXISTS idx_trips_route ON trips (route_id, shift_code);
CREATE INDEX IF NOT EXISTS idx_trips_start ON trips (start_time);

-- Quien ejecuto realmente el recorrido
CREATE TABLE IF NOT EXISTS operations (
    trip_id            BIGINT  NOT NULL,
    guide_id           BIGINT  NOT NULL,
    is_primary_on_trip BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (trip_id, guide_id),
    CONSTRAINT fk_operations_trips
        FOREIGN KEY (trip_id) REFERENCES trips (trip_id)
);
CREATE INDEX IF NOT EXISTS idx_operations_guide ON operations (guide_id);

CREATE TABLE IF NOT EXISTS attendances (
    trip_id    BIGINT      NOT NULL,
    student_id BIGINT      NOT NULL,
    is_present BOOLEAN     NOT NULL DEFAULT FALSE,
    roll_call  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (trip_id, student_id),
    CONSTRAINT fk_attendances_trips
        FOREIGN KEY (trip_id) REFERENCES trips (trip_id)
);
CREATE INDEX IF NOT EXISTS idx_attendances_student ON attendances (student_id);

-- Llegada tarde con afectacion al servicio
CREATE TABLE IF NOT EXISTS impact_arrivals (
    impact_arrival_id BIGINT      GENERATED ALWAYS AS IDENTITY,
    arrival_date      TIMESTAMPTZ NOT NULL,
    status            VARCHAR(20) NOT NULL,
    detail            TEXT        NULL,
    guide_id          BIGINT      NOT NULL,
    PRIMARY KEY (impact_arrival_id),
    CONSTRAINT ck_impact_status CHECK (status IN ('OPEN','NOTIFIED','MINUTED'))
);
CREATE INDEX IF NOT EXISTS idx_impact_guide ON impact_arrivals (guide_id);

-- Llegada tarde sin afectacion. counter acumula para el escalamiento
CREATE TABLE IF NOT EXISTS non_impact_arrivals (
    non_impact_arrival_id BIGINT      GENERATED ALWAYS AS IDENTITY,
    arrival_date          TIMESTAMPTZ NOT NULL,
    status                VARCHAR(20) NOT NULL,
    counter               INTEGER     NOT NULL DEFAULT 0,
    impact_arrival_id     BIGINT      NULL,
    guide_id              BIGINT      NOT NULL,
    PRIMARY KEY (non_impact_arrival_id),
    CONSTRAINT ck_non_impact_status CHECK (status IN ('PENDING','NOTIFIED')),
    CONSTRAINT fk_nia_impact
        FOREIGN KEY (impact_arrival_id) REFERENCES impact_arrivals (impact_arrival_id)
);
CREATE INDEX IF NOT EXISTS idx_non_impact_guide ON non_impact_arrivals (guide_id);

-- Llegada del guia al punto de inicio
CREATE TABLE IF NOT EXISTS guide_check_ins (
    check_in_id   BIGINT       GENERATED ALWAYS AS IDENTITY,
    check_in_time TIMESTAMPTZ  NOT NULL,
    address       VARCHAR(200) NOT NULL,
    latitude      DOUBLE PRECISION NOT NULL,
    longitude     DOUBLE PRECISION NOT NULL,
    route_id      VARCHAR(4)   NOT NULL,
    shift_code    VARCHAR(3)   NOT NULL,
    guide_id      BIGINT       NOT NULL,
    is_valid      BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (check_in_id),
    CONSTRAINT ck_checkin_shift CHECK (shift_code IN ('541','542'))
);
CREATE INDEX IF NOT EXISTS idx_checkin_guide ON guide_check_ins (guide_id);
CREATE INDEX IF NOT EXISTS idx_checkin_route ON guide_check_ins (route_id, shift_code);

-- Codigos reportados durante un recorrido
CREATE TABLE IF NOT EXISTS trip_events (
    event_id     BIGINT      GENERATED ALWAYS AS IDENTITY,
    event_time   TIMESTAMPTZ NOT NULL,
    latitude     DOUBLE PRECISION NULL,
    longitude    DOUBLE PRECISION NULL,
    observations TEXT        NULL,
    trip_id      BIGINT      NOT NULL,
    guide_id     BIGINT      NOT NULL,
    code_id      INTEGER     NOT NULL,
    PRIMARY KEY (event_id),
    CONSTRAINT fk_te_trips
        FOREIGN KEY (trip_id) REFERENCES trips (trip_id),
    CONSTRAINT fk_te_action_codes
        FOREIGN KEY (code_id) REFERENCES action_codes (code_id)
);
CREATE INDEX IF NOT EXISTS idx_te_trip  ON trip_events (trip_id);
CREATE INDEX IF NOT EXISTS idx_te_guide ON trip_events (guide_id);

INSERT INTO action_codes (code_id, description) VALUES
    (58,  'DISPONIBLE PARA SERVICIO'),
    (512, 'INFORME NOVEDADES'),
    (520, 'UBICACION ACTUAL'),
    (523, 'HE LLEGADO AL SITIO'),
    (524, 'MISION CUMPLIDA'),
    (537, 'APOYE POLICIA'),
    (541, 'RUTA CONFIANZA HACIA EL COLEGIO'),
    (542, 'RUTA CONFIANZA SALIDA DEL COLEGIO'),
    (550, 'ACCIDENTE GUIA EN BICICLETA')
ON CONFLICT (code_id) DO UPDATE SET description = EXCLUDED.description;
