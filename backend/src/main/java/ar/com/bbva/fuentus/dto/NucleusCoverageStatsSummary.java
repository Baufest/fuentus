package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NucleusCoverageStatsSummary {
    private String label;
    private double coveragePercentage;
    private NucleusLevel nucleusLevel;
}
