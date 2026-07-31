package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.ChimeraSastImportDTO;
import ar.com.bbva.fuentus.entities.ChimeraSast;
import ar.com.bbva.fuentus.repositories.ChimeraSastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ChimeraSastProcessor implements EntityProcessor {

    @Autowired
    private ChimeraSastRepository chimeraSastRepository;

    @Override
    @Transactional
    public boolean process(Object dto) {
        if (!(dto instanceof ChimeraSastImportDTO)) {
            return false;
        }
        
        ChimeraSastImportDTO sastDTO = (ChimeraSastImportDTO) dto;
        
        try {
            System.out.println("--- Procesando ChimeraSast ---");
            System.out.println("ProjectId: " + sastDTO.getProjectId());
            System.out.println("Name: " + sastDTO.getName());
            System.out.println("UUAA: " + sastDTO.getUuaa());
            
            // Validar campos obligatorios
            if (!isValidChimeraSastDTO(sastDTO)) {
                System.err.println("DTO inválido, saltando...");
                return false;
            }
            
            // Buscar si ya existe un registro con el mismo projectId y name
            Optional<ChimeraSast> existingSast = chimeraSastRepository
                .findByProjectIdAndName(sastDTO.getProjectId(), sastDTO.getName());
            
            ChimeraSast sast;
            if (existingSast.isPresent()) {
                // Update existing record
                sast = existingSast.get();
                updateChimeraSastFromDTO(sastDTO, sast);
                System.out.println("Actualizando registro existente con projectId: " + sastDTO.getProjectId() + 
                                 ", name: " + sastDTO.getName());
            } else {
                // Create new record
                sast = new ChimeraSast();
                updateChimeraSastFromDTO(sastDTO, sast);
                System.out.println("Creando nuevo registro con projectId: " + sastDTO.getProjectId() + 
                                 ", name: " + sastDTO.getName());
            }
            
            // Guardar
            ChimeraSast savedSast = chimeraSastRepository.saveAndFlush(sast);
            System.out.println("Guardado exitoso con projectId: " + savedSast.getProjectId());
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error procesando ChimeraSastDTO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update ChimeraSast entity from DTO
     */
    private void updateChimeraSastFromDTO(ChimeraSastImportDTO dto, ChimeraSast entity) {
        entity.setProjectId(dto.getProjectId());
        entity.setName(dto.getName());
        entity.setRepoUrl(dto.getRepoUrl());
        entity.setUuaa(dto.getUuaa());
        entity.setCountryDetName(dto.getCountryDetName());
        entity.setApplication(dto.getApplication());
        
        // Set last_scan directly (now it's LocalDateTime)
        entity.setLastScan(dto.getLastScan());
        
        entity.setChimeraUrl(dto.getChimeraUrl());
        entity.setBranch(dto.getBranch());
        entity.setAnalyzer(dto.getAnalyzer());
        entity.setArq(dto.getArq());
        entity.setLanguage(dto.getLanguage());
        
        // Parse stock_flow enum
        if (dto.getStockFlow() != null && !dto.getStockFlow().trim().isEmpty()) {
            try {
                entity.setStockFlow(ChimeraSast.StockFlow.valueOf(dto.getStockFlow().toLowerCase()));
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid stock_flow value: '" + dto.getStockFlow() + "'. Must be 'stock' or 'flow'. Setting to null.");
                entity.setStockFlow(null);
            }
        } else {
            entity.setStockFlow(null);
        }
        
        // Set counters with defaults
        entity.setAssumed1(dto.getAssumed1() != null ? dto.getAssumed1() : 0);
        entity.setAssumed2(dto.getAssumed2() != null ? dto.getAssumed2() : 0);
        entity.setHigh(dto.getHigh() != null ? dto.getHigh() : 0);
        entity.setCritical(dto.getCritical() != null ? dto.getCritical() : 0);
        entity.setMedium(dto.getMedium() != null ? dto.getMedium() : 0);
        entity.setLow(dto.getLow() != null ? dto.getLow() : 0);
        entity.setToReview(dto.getToReview() != null ? dto.getToReview() : 0);
        entity.setLines(dto.getLines() != null ? dto.getLines() : 0);
    }
    
    /**
     * Validate that the ChimeraSastDTO has required fields
     */
    private boolean isValidChimeraSastDTO(ChimeraSastImportDTO dto) {
        if (dto.getProjectId() == null || dto.getProjectId().trim().isEmpty()) {
            System.err.println("ProjectId es obligatorio");
            return false;
        }
        
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            System.err.println("Name es obligatorio");
            return false;
        }
        
        return true;
    }
}