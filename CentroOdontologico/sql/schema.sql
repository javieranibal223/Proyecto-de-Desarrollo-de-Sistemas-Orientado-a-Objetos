-- Esquema de la base centro_odontologico (MySQL 8.x, InnoDB)
CREATE DATABASE IF NOT EXISTS centro_odontologico
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE centro_odontologico;

CREATE TABLE IF NOT EXISTS pacientes (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(80) NOT NULL,
    apellido      VARCHAR(80) NOT NULL,
    dni           VARCHAR(8)  NOT NULL UNIQUE,
    fecha_ingreso DATE        NOT NULL
) ENGINE = InnoDB;

-- Próximas entidades (se usan al migrar Odontólogo y Turno)
CREATE TABLE IF NOT EXISTS odontologos (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    matricula     VARCHAR(20) NOT NULL UNIQUE,
    nombre        VARCHAR(80) NOT NULL,
    apellido      VARCHAR(80) NOT NULL,
    especialidad  VARCHAR(60) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS turnos (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id   INT NOT NULL,
    odontologo_id INT NOT NULL,
    fecha_hora    DATETIME NOT NULL,
    motivo        VARCHAR(200),
    CONSTRAINT fk_turno_paciente   FOREIGN KEY (paciente_id)   REFERENCES pacientes(id)   ON DELETE RESTRICT,
    CONSTRAINT fk_turno_odontologo FOREIGN KEY (odontologo_id) REFERENCES odontologos(id) ON DELETE RESTRICT,
    CONSTRAINT uq_turno_odontologo_hora UNIQUE (odontologo_id, fecha_hora)
) ENGINE = InnoDB;
