package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalDTO {
    private Long id;
    private String category;
    private String name;
    private String description;
    private Double numericValue;
    private String categoricalValue;
    private String unit;
    private Integer displayOrder;
}
