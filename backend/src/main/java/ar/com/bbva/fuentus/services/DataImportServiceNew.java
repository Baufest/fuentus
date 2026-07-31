package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.DTOParserService;
import ar.com.bbva.fuentus.dto.ImportResultDTO;
import ar.com.bbva.fuentus.enums.EntityType;
import ar.com.bbva.fuentus.processors.EntityProcessor;
import ar.com.bbva.fuentus.processors.ProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class DataImportServiceNew {

    @Autowired
    private DTOParserService dtoParserService;
    
    @Autowired
    private ProcessorFactory processorFactory;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public ImportResultDTO importData(MultipartFile file, EntityType entityType, String format) {
        System.out.println("=== INICIANDO IMPORTACIÓN ===");
        System.out.println("Entidad: " + entityType);
        System.out.println("Formato: " + format);
        System.out.println("Archivo: " + file.getOriginalFilename());
        
        ImportResultDTO result = new ImportResultDTO();
        
        try {
            // Parsear archivo según el formato
            List<?> dtos;
            if ("csv".equalsIgnoreCase(format)) {
                dtos = dtoParserService.parseCsvFile(file, entityType);
            } else if ("json".equalsIgnoreCase(format)) {
                dtos = dtoParserService.parseJsonFile(file, entityType);
            } else {
                throw new IllegalArgumentException("Formato no soportado: " + format);
            }
            
            result.setTotalProcessed(dtos.size());
            
            // Procesar DTOs usando el procesador apropiado
            EntityProcessor processor = processorFactory.getProcessor(entityType);
            
            int successCount = 0;
            int errorCount = 0;
            StringBuilder errorMessages = new StringBuilder();
            
            for (Object dto : dtos) {
                try {
                    // Usar una nueva transacción para cada registro
                    entityManager.clear();
                    
                    boolean success = processor.process(dto);
                    if (success) {
                        successCount++;
                    } else {
                        errorCount++;
                        errorMessages.append("Error procesando registro. ");
                    }
                    
                } catch (Exception e) {
                    System.err.println("Error procesando registro: " + e.getMessage());
                    e.printStackTrace();
                    errorCount++;
                    errorMessages.append("Error: ").append(e.getMessage()).append("; ");
                }
            }
            
            result.setSuccessfulImports(successCount);
            
            if (errorCount > 0) {
                result.setErrorMessage("Importación completada con errores. " + 
                    successCount + " exitosos, " + errorCount + " fallidos. " + errorMessages.toString());
            } else {
                result.setErrorMessage(null); // No hay errores
            }
            
            System.out.println("=== IMPORTACIÓN FINALIZADA ===");
            System.out.println("Total: " + result.getTotalProcessed());
            System.out.println("Exitosos: " + successCount);
            System.out.println("Fallidos: " + errorCount);
            
            return result;
            
        } catch (Exception e) {
            System.err.println("Error durante importación: " + e.getMessage());
            e.printStackTrace();
            
            result.setTotalProcessed(0);
            result.setSuccessfulImports(0);
            result.setErrorMessage("Error durante la importación: " + e.getMessage());
            
            return result;
        }
    }
}