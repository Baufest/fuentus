package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.ChimeraScaImportDTO;
import ar.com.bbva.fuentus.entities.ChimeraSca;
import ar.com.bbva.fuentus.repositories.ChimeraScaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ChimeraScaProcessor implements EntityProcessor {

    @Autowired
    private ChimeraScaRepository chimeraScaRepository;

    @Override
    @Transactional
    public boolean process(Object dto) {
        if (!(dto instanceof ChimeraScaImportDTO)) {
            return false;
        }
        
        ChimeraScaImportDTO chimeraScaDTO = (ChimeraScaImportDTO) dto;
        
        try {
            System.out.println("--- Procesando ChimeraSca ---");
            System.out.println("ProjectId: " + chimeraScaDTO.getProjectId());
            System.out.println("Name: " + chimeraScaDTO.getName());
            System.out.println("UUAA: " + chimeraScaDTO.getUuaa());
            
            // Validar campos obligatorios
            if (!isValidChimeraScaDTO(chimeraScaDTO)) {
                System.err.println("DTO inválido, saltando...");
                return false;
            }
            
            // Buscar si ya existe
            Optional<ChimeraSca> existingOpt = Optional.empty();
            if (chimeraScaDTO.getProjectId() != null && chimeraScaDTO.getName() != null) {
                existingOpt = chimeraScaRepository.findByProjectIdAndName(
                    chimeraScaDTO.getProjectId(), 
                    chimeraScaDTO.getName()
                );
            }
            
            ChimeraSca chimeraSca;
            
            if (existingOpt.isPresent()) {
                // UPDATE: usar la entidad existente
                chimeraSca = existingOpt.get();
                System.out.println("Actualizando registro existente con ID: " + chimeraSca.getId());
                updateChimeraScaFromDTO(chimeraScaDTO, chimeraSca);
            } else {
                // INSERT: crear nueva entidad
                System.out.println("Creando nuevo registro");
                chimeraSca = new ChimeraSca();
                updateChimeraScaFromDTO(chimeraScaDTO, chimeraSca);
            }
            
            // Guardar
            ChimeraSca savedChimeraSca = chimeraScaRepository.saveAndFlush(chimeraSca);
            System.out.println("Guardado exitoso con ID: " + savedChimeraSca.getId());
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error procesando ChimeraScaDTO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean isValidChimeraScaDTO(ChimeraScaImportDTO dto) {
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
    
    private void updateChimeraScaFromDTO(ChimeraScaImportDTO dto, ChimeraSca entity) {
        entity.setProjectId(dto.getProjectId());
        entity.setAppId(dto.getAppId());
        entity.setName(dto.getName());
        entity.setUuaa(dto.getUuaa());
        entity.setLow(dto.getLow());
        entity.setMedium(dto.getMedium());
        entity.setHigh(dto.getHigh());
        entity.setCritical(dto.getCritical());
    }
}