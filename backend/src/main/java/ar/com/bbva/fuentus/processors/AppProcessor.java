package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.AppImportDTO;
import ar.com.bbva.fuentus.entities.App;
import ar.com.bbva.fuentus.mappers.DataImportMapper;
import ar.com.bbva.fuentus.repositories.AppsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class AppProcessor implements EntityProcessor {

    @Autowired
    private AppsRepository appsRepository;
    
    @Autowired
    private DataImportMapper dataImportMapper;

    @Override
    public boolean process(Object dto) {
        if (!(dto instanceof AppImportDTO)) {
            throw new IllegalArgumentException("Expected AppImportDTO but got: " + 
                (dto != null ? dto.getClass().getSimpleName() : "null"));
        }
        
        AppImportDTO appDTO = (AppImportDTO) dto;
        
        try {
            // Validación temprana: name y projectId son obligatorios para el criterio de unicidad
            if (appDTO.getName() == null || appDTO.getName().trim().isEmpty()) {
                System.err.println("ERROR: name es obligatorio. Entrada rechazada.");
                return false;
            }
            
            if (appDTO.getProjectId() == null || appDTO.getProjectId().trim().isEmpty()) {
                System.err.println("ERROR: projectId es obligatorio. Entrada rechazada.");
                return false;
            }
            
            System.out.println("--- Procesando DTO con name: " + appDTO.getName() + ", projectId: " + appDTO.getProjectId() + " ---");
            
            // Buscar si existe un registro con esa combinación name + projectId
            App existingApp = appsRepository.findByNameAndProjectId(appDTO.getName(), appDTO.getProjectId());
            
            App app;
            if (existingApp != null) {
                // UPDATE: Registro existente
                app = existingApp;
                dataImportMapper.updateAppFromDTO(appDTO, app);
                System.out.println("Actualizando App existente con ID: " + app.getId() + 
                                 ", name: " + app.getName() + ", projectId: " + app.getProjectId());
            } else {
                // INSERT: Nuevo registro (ID será auto-generado)
                app = dataImportMapper.toApp(appDTO);
                System.out.println("Creando nueva App con name: " + app.getName() + 
                                 ", projectId: " + app.getProjectId());
            }
            
            // Validar campos requeridos adicionales
            if (!isValidApp(app)) {
                System.err.println("ERROR: Datos inválidos. Entrada rechazada.");
                return false;
            }
            
            appsRepository.save(app);
            System.out.println("✅ App procesada exitosamente con ID: " + app.getId() + 
                             ", name: " + app.getName() + ", projectId: " + app.getProjectId());
            return true;
            
        } catch (DataIntegrityViolationException e) {
            System.err.println("ERROR: Violación de integridad para name=" + appDTO.getName() + 
                             ", projectId=" + appDTO.getProjectId() + ": " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("ERROR: Error inesperado procesando name=" + appDTO.getName() + 
                             ", projectId=" + appDTO.getProjectId() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Validate that the App has required fields
     */
    private boolean isValidApp(App app) {
        if (app.getName() == null || app.getName().trim().isEmpty()) {
            System.err.println("name es obligatorio");
            return false;
        }
        
        if (app.getProjectId() == null || app.getProjectId().trim().isEmpty()) {
            System.err.println("projectId es obligatorio");
            return false;
        }
        
        return true;
    }
}