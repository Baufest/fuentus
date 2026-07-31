package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO que contiene el ranking top 3 de un nivel organizacional específico
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingDTO {
    private String nivelTipo; // VERTICAL, FABRICA, SN1, SN2
    private String metricaTipo; // PRODUCTIVIDAD, LT, CT
    private List<RankingItemDTO> ranking;
}
