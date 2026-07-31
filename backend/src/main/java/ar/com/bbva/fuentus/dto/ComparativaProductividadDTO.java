package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO que contiene datos comparativos de productividad por niveles organizacionales
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComparativaProductividadDTO {
    private String nivelFiltrado; // Qué nivel está filtrado (VERTICAL, FABRICA, SN1, etc)
    private List<GraficoNivelDTO> graficos; // Gráficos de los subniveles
}
