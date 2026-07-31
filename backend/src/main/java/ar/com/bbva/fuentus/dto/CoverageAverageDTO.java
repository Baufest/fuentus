package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoverageAverageDTO {
    private String vertical;
    private String uol2;
    private Double averageCoverage;
    private Long totalProjects;
    private String filterType; // "vertical" o "uol2"
}