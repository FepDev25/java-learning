-- Esquema inicial para los ejemplos JDBC
-- Ejecutado automáticamente al levantar el contenedor Docker

CREATE TABLE IF NOT EXISTS estudiantes (
    id        SERIAL PRIMARY KEY,
    nombre    VARCHAR(100) NOT NULL,
    edad      INTEGER      NOT NULL CHECK (edad BETWEEN 15 AND 99),
    pais      VARCHAR(100) NOT NULL DEFAULT 'Ecuador',
    carrera   VARCHAR(150) NOT NULL,
    promedio  NUMERIC(4,2)         CHECK (promedio BETWEEN 0 AND 10),
    activo    BOOLEAN      NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS materias (
    id       SERIAL PRIMARY KEY,
    nombre   VARCHAR(150) NOT NULL UNIQUE,
    creditos INTEGER      NOT NULL DEFAULT 4
);

CREATE TABLE IF NOT EXISTS inscripciones (
    id             SERIAL PRIMARY KEY,
    estudiante_id  INTEGER        NOT NULL REFERENCES estudiantes(id) ON DELETE CASCADE,
    materia_id     INTEGER        NOT NULL REFERENCES materias(id),
    nota           NUMERIC(4,2)   CHECK (nota BETWEEN 0 AND 10),
    semestre       VARCHAR(10)    NOT NULL,
    UNIQUE (estudiante_id, materia_id, semestre)
);

-- Datos de prueba
INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio) VALUES
    ('Felipe',  21, 'Ecuador', 'Ciencias de la Computación', 9.20),
    ('Sofía',   22, 'Ecuador', 'Ingeniería de Software',     8.75),
    ('Mateo',   20, 'Ecuador', 'Matemáticas',                9.50),
    ('Valentina',23, 'Ecuador', 'Ciencias de la Computación', 8.90),
    ('Diego',   21, 'Ecuador', 'Redes y Telecomunicaciones', 7.80);

INSERT INTO materias (nombre, creditos) VALUES
    ('Algoritmos y Estructuras',  4),
    ('Redes de Computadoras',     3),
    ('Bases de Datos',            4),
    ('Sistemas Operativos',       4),
    ('Inteligencia Artificial',   3);

INSERT INTO inscripciones (estudiante_id, materia_id, nota, semestre) VALUES
    (1, 1, 9.5, '2024-1'),
    (1, 2, 8.8, '2024-1'),
    (1, 3, 9.1, '2024-2'),
    (2, 1, 8.5, '2024-1'),
    (2, 4, 9.0, '2024-2'),
    (3, 5, 9.7, '2024-2');
