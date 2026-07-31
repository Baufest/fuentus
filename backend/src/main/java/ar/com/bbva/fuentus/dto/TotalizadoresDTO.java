package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO que contiene todos los totalizadores con los mejores niveles organizacionales
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalizadoresDTO {
    private List<TopLevelMetricsDTO> mejoresNiveles;
}
