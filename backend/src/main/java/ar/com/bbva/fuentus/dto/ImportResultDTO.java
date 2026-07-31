package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportResultDTO {
    private int totalProcessed;
    private int successfulImports;
    private String errorMessage;
    
    public int getFailedImports() {
        return totalProcessed - successfulImports;
    }
    
    public boolean hasErrors() {
        return errorMessage != null && !errorMessage.trim().isEmpty();
    }
    
    public double getSuccessRate() {
        if (totalProcessed == 0) return 0.0;
        return (double) successfulImports / totalProcessed * 100.0;
    }
}