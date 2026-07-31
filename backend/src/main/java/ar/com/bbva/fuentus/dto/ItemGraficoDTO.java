package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un item individual en el gráfico
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemGraficoDTO {
    private String nombre; // Nombre del nivel organizacional
    private Double productividad;
    private Double promedioLT;
    private Double promedioCT;
    private Integer totalFeatures;
    private Double totalFTEs;
}
