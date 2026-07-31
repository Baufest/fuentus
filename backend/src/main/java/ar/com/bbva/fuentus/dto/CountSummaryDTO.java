package ar.com.bbva.fuentus.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountSummaryDTO {
    
    private int totalVerticales;
    private int totalFabricas; // orgN2fabrica
    private int totalSn1; // serviceN1
    private int totalSn2; // serviceN2
    private int totalUuaas;
}