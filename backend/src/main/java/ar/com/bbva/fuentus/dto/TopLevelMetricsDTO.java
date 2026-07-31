package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa las métricas del mejor nivel organizacional por tipo de métrica
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopLevelMetricsDTO {
    private String nivelTipo; // VERTICAL, FABRICA, SN1, SN2
    private String metricaTipo; // PRODUCTIVIDAD, LT, CT
    private String nombre;
    private Double valor;
}
