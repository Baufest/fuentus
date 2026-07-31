package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un item individual en el ranking
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingItemDTO {
    private Integer posicion; // 1, 2, 3
    private String nombre;
    private Double valor;
}
