package ar.com.bbva.fuentus.dto;

import ar.com.bbva.fuentus.entities.Vertical;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FactoryVertical {
    
    private String orgN2;
    private Vertical vertical;
    
    public FactoryVertical(String orgN1, Vertical vertical) {
        this.orgN2 = orgN1;
        this.vertical = vertical;
    }

}