package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeudaTecnicaItemDTO {
    private String label;           // Nombre del nivel (vertical, fabrica, sn1, sn2)
    private String nivelTipo;       // VERTICAL, FABRICA, SN1, SN2
    private Integer totalBugs;
    private Integer totalVulnerabilities;
    private Integer totalCodeSmells;
    private Integer totalApps;      // Cantidad de apps en ese nivel
}
