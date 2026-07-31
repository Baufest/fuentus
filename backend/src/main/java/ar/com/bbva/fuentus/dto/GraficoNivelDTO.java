package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO que representa un gráfico de comparación para un nivel organizacional
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraficoNivelDTO {
    private String nivelTipo; // FABRICA, SN1, SN2
    private String titulo; // Título del gráfico
    private List<ItemGraficoDTO> items; // Datos para el gráfico
}
