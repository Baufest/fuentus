-- Crear tabla velocidad
CREATE TABLE IF NOT EXISTS velocidad (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nucleus_id BIGINT NULL,
    lt INT NULL COMMENT 'Lead Time - Tiempo desde inicio hasta despliegue',
    ct INT NULL COMMENT 'Cycle Time - Tiempo desde inicio de desarrollo hasta despliegue',
    date DATE NOT NULL COMMENT 'Fecha más reciente extraída de las columnas de fecha del CSV',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign Key a nucleus_services
    CONSTRAINT fk_velocidad_nucleus 
        FOREIGN KEY (nucleus_id) 
        REFERENCES nucleus_services(Id_Fullservice)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    
    -- Índice único para evitar duplicados por servicio y fecha
    UNIQUE KEY uk_velocidad_nucleus_date (nucleus_id, date),
    
    -- Índice para búsquedas por fecha
    INDEX idx_velocidad_date (date),
    
    -- Índice para búsquedas por servicio
    INDEX idx_velocidad_nucleus (nucleus_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Tabla de velocidad de features: Lead Time y Cycle Time por servicio';
