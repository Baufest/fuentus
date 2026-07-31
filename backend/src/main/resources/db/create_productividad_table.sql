-- Script SQL para crear la tabla de Productividad
-- Esta tabla almacena métricas de productividad de servicios

CREATE TABLE IF NOT EXISTS productividad (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nucleus_id BIGINT,
    features INT,
    ftes_directos DOUBLE,
    ftes_indirectos DOUBLE,
    fecha DATE NOT NULL,
    FOREIGN KEY (nucleus_id) REFERENCES nucleus_services(Id_Fullservice) ON DELETE SET NULL,
    INDEX idx_fecha (fecha),
    INDEX idx_nucleus_id (nucleus_id),
    INDEX idx_nucleus_fecha (nucleus_id, fecha)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios sobre las columnas
ALTER TABLE productividad 
    MODIFY COLUMN id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID autogenerado de la tabla',
    MODIFY COLUMN nucleus_id BIGINT COMMENT 'Foreign key a nucleus_services (Id_Fullservice)',
    MODIFY COLUMN features INT COMMENT 'Cantidad de features del servicio',
    MODIFY COLUMN ftes_directos DOUBLE COMMENT 'FTEs (Full-Time Equivalents) directos',
    MODIFY COLUMN ftes_indirectos DOUBLE COMMENT 'FTEs (Full-Time Equivalents) indirectos',
    MODIFY COLUMN fecha DATE NOT NULL COMMENT 'Fecha de los datos de productividad';
