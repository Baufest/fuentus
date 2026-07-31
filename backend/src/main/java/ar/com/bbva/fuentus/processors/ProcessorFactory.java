package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.enums.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessorFactory {

    @Autowired
    private NucleusProcessor nucleusProcessor;
    
    @Autowired
    private ChimeraScaProcessor chimeraScaProcessor;
    
    @Autowired
    private ChimeraSastProcessor chimeraSastProcessor;
    
    @Autowired
    private AppProcessor appProcessor;
    
    @Autowired
    private RfoProcessor rfoProcessor;
    
    @Autowired
    private ProductividadProcessor productividadProcessor;
    
    @Autowired
    private VelocidadProcessor velocidadProcessor;

    public EntityProcessor getProcessor(EntityType entityType) {
        switch (entityType) {
            case NUCLEUS_SERVICES:
                return nucleusProcessor;
            case CHIMERA_SCA:
                return chimeraScaProcessor;
            case CHIMERA_SAST:
                return chimeraSastProcessor;
            case APPS:
                return appProcessor;
            case RFO:
                return rfoProcessor;
            case PRODUCTIVIDAD:
                return productividadProcessor;
            case VELOCIDAD:
                return velocidadProcessor;
            default:
                throw new IllegalArgumentException("Unknown entity type: " + entityType);
        }
    }
}