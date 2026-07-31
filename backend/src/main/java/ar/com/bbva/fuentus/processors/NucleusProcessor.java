package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.NucleusImportDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.mappers.DataImportMapper;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NucleusProcessor implements EntityProcessor {

    @Autowired
    private NucleusRepository nucleusRepository;
    
    @Autowired
    private DataImportMapper dataImportMapper;

    @Override
    public boolean process(Object dto) {
        if (!(dto instanceof NucleusImportDTO)) {
            throw new IllegalArgumentException("Expected NucleusImportDTO but got: " + 
                (dto != null ? dto.getClass().getSimpleName() : "null"));
        }
        
        NucleusImportDTO nucleusDTO = (NucleusImportDTO) dto;
        
        try {
            // Validación temprana: Id_Fullservice debe estar presente
            if (nucleusDTO.getIdFullservice() == null) {
                System.err.println("ERROR: Id_Fullservice es obligatorio. Entrada rechazada.");
                return false;
            }
            
            if (nucleusDTO.getIdFullservice() <= 0) {
                System.err.println("ERROR: Id_Fullservice debe ser positivo. Recibido: " + nucleusDTO.getIdFullservice() + ". Entrada rechazada.");
                return false;
            }
            
            System.out.println("--- Procesando DTO con Id_Fullservice: " + nucleusDTO.getIdFullservice() + " ---");
            System.out.println("ServiceN1: " + nucleusDTO.getServiceN1());
            System.out.println("ServiceN2: " + nucleusDTO.getServiceN2());
            
            // Buscar si existe un registro con ese Id_Fullservice
            Optional<Nucleus> existingNucleus = nucleusRepository.findById(nucleusDTO.getIdFullservice());
            
            Nucleus nucleus;
            if (existingNucleus.isPresent()) {
                // UPDATE: Registro existente
                nucleus = existingNucleus.get();
                dataImportMapper.updateNucleusFromDTO(nucleusDTO, nucleus);
                System.out.println("Actualizando Nucleus existente con Id_Fullservice: " + nucleus.getId());
            } else {
                // INSERT: Nuevo registro
                nucleus = dataImportMapper.toNucleus(nucleusDTO);
                System.out.println("Creando nuevo Nucleus con Id_Fullservice: " + nucleus.getId());
            }
            
            // Validar campos requeridos adicionales
            if (!isValidNucleus(nucleus)) {
                System.err.println("ERROR: Datos inválidos. Entrada rechazada.");
                return false;
            }
            
            nucleusRepository.save(nucleus);
            System.out.println("✅ Nucleus procesado exitosamente con Id_Fullservice: " + nucleus.getId());
            return true;
            
        } catch (DataIntegrityViolationException e) {
            System.err.println("ERROR: Violación de integridad para Id_Fullservice " + nucleusDTO.getIdFullservice() + ": " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("ERROR: Error inesperado procesando Id_Fullservice " + nucleusDTO.getIdFullservice() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Validate that the Nucleus has required fields
     */
    private boolean isValidNucleus(Nucleus nucleus) {
        if (nucleus.getId() == null) {
            System.err.println("Id_Fullservice es obligatorio");
            return false;
        }
        
        if (nucleus.getServiceN1() == null || nucleus.getServiceN1().trim().isEmpty()) {
            System.err.println("ServiceN1 es obligatorio");
            return false;
        }
        
        if (nucleus.getServiceN2() == null || nucleus.getServiceN2().trim().isEmpty()) {
            System.err.println("ServiceN2 es obligatorio");
            return false;
        }
        
        return true;
    }
}