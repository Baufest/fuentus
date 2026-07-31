package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.RfoImportDTO;
import ar.com.bbva.fuentus.entities.Rfo;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.mappers.DataImportMapper;
import ar.com.bbva.fuentus.repositories.RfoRepository;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RfoProcessor implements EntityProcessor {

    private static int processedCount = 0;
    private static int successCount = 0;

    @Autowired
    private RfoRepository rfoRepository;
    
    @Autowired
    private NucleusRepository nucleusRepository;
    
    @Autowired
    private DataImportMapper dataImportMapper;

    @Override
    public boolean process(Object dto) {
        if (!(dto instanceof RfoImportDTO)) {
            throw new IllegalArgumentException("Expected RfoImportDTO but got: " + 
                (dto != null ? dto.getClass().getSimpleName() : "null"));
        }
        
        RfoImportDTO rfoDTO = (RfoImportDTO) dto;
        processedCount++;
        
        try {
            System.out.println("=== PROCESANDO REGISTRO #" + processedCount + " ===");
            
            // ✅ DEBUG: Mostrar todos los datos del DTO
            System.out.println("DEBUG DTO:");
            System.out.println("  - RFO ID: '" + rfoDTO.getRfoId() + "'");
            System.out.println("  - SEVICIO N2: '" + rfoDTO.getSevicioN2() + "'");
            System.out.println("  - EMAIL: '" + rfoDTO.getEmail() + "'");
            System.out.println("  - ESTADO RFO: '" + rfoDTO.getEstadoRfo() + "'");
            System.out.println("  - FECHA PUESTA EN PRODUCCION: '" + rfoDTO.getFechaPuestaProduccion() + "'");
            
            // Validación temprana: rfoId es obligatorio
            if (rfoDTO.getRfoId() == null) {
                System.err.println("ERROR: RFO ID es obligatorio. Entrada rechazada. Registro #" + processedCount);
                return false;
            }
            
            if (rfoDTO.getRfoId() <= 0) {
                System.err.println("ERROR: RFO ID debe ser positivo. Recibido: " + rfoDTO.getRfoId() + ". Entrada rechazada. Registro #" + processedCount);
                return false;
            }
            
            System.out.println("--- Procesando RFO con ID: " + rfoDTO.getRfoId() + " ---");
            System.out.println("SEVICIO N2: " + rfoDTO.getSevicioN2());
            
            // Buscar si existe un RFO con ese ID
            Rfo existingRfo = rfoRepository.findByRfoId(rfoDTO.getRfoId());
            
            Rfo rfo;
            if (existingRfo != null) {
                // Capturar el estado original ANTES de actualizar
                String estadoActual = existingRfo.getEstadoRfo();
                String nuevoEstado = rfoDTO.getEstadoRfo();
                
                // Siempre llamar al mapper para actualizar todos los campos
                dataImportMapper.updateRfoFromDTO(rfoDTO, existingRfo);
                
                // Verificar si el estado cambió (ignorando mayúsculas, acentos y espacios)
                if (!estadosIguales(nuevoEstado, estadoActual)) {
                    System.out.println("Actualizando estado de RFO existente con ID: " + existingRfo.getRfoId() +
                        " de '" + estadoActual + "' a '" + nuevoEstado + "'.");
                } else {
                    System.out.println("RFO existente con ID: " + existingRfo.getRfoId() + " ya tiene el mismo estado ('" + estadoActual + "'). No se actualiza.");
                    return true;
                }
                rfo = existingRfo;
            } else {
                // INSERT: Nuevo RFO
                rfo = dataImportMapper.toRfo(rfoDTO);
                System.out.println("Creando nuevo RFO con ID: " + rfo.getRfoId());
            }
            
            // Buscar Nucleus asociado por sevicioN2 - VALIDACIÓN OBLIGATORIA
            if (rfoDTO.getSevicioN2() != null && !rfoDTO.getSevicioN2().trim().isEmpty()) {
                Optional<Nucleus> nucleusOpt = nucleusRepository.findFirstByServiceN2(rfoDTO.getSevicioN2().trim());
                if (nucleusOpt.isPresent()) {
                    rfo.setNucleus(nucleusOpt.get());
                    System.out.println("✅ RFO asociado a Nucleus ID: " + nucleusOpt.get().getId() + 
                                     " (serviceN2: " + nucleusOpt.get().getServiceN2() + ")");
                } else {
                    // NUEVO: Si no existe el SN2 en nucleus_services, rechazar el RFO
                    System.err.println("❌ ERROR: No se encontró ningún registro en nucleus_services con SN2: '" + 
                                     rfoDTO.getSevicioN2() + "'. RFO rechazado según nueva regla de negocio. Registro #" + processedCount);
                    return false;
                }
            } else {
                // NUEVO: Si SN2 está vacío, también rechazar el RFO
                System.err.println("❌ ERROR: SEVICIO N2 es obligatorio para validar existencia en nucleus_services. RFO rechazado. Registro #" + processedCount);
                return false;
            }
            
            // ✅ DEBUG: Antes de validar
            System.out.println("DEBUG: Antes de isValidRfo()");
            System.out.println("  - RFO ID final: " + rfo.getRfoId());
            System.out.println("  - EMAIL final: '" + rfo.getEmail() + "'");
            System.out.println("  - ESTADO RFO final: '" + rfo.getEstadoRfo() + "'");
            
            // Validar campos requeridos adicionales
            if (!isValidRfo(rfo)) {
                System.err.println("ERROR: Datos inválidos. Entrada rechazada. Registro #" + processedCount);
                return false;
            }
            
            System.out.println("DEBUG: Pasó isValidRfo(), ejecutando save()...");
            
            // ✅ DEBUG: Contar registros antes del save
            long countBefore = rfoRepository.count();
            System.out.println("DEBUG: Registros en tabla ANTES del save: " + countBefore);
            
            rfoRepository.save(rfo);
            
            // ✅ DEBUG: Contar registros después del save
            long countAfter = rfoRepository.count();
            System.out.println("DEBUG: Registros en tabla DESPUÉS del save: " + countAfter);
            
            successCount++;
            System.out.println("✅ ÉXITO #" + successCount + " - RFO procesado con ID: " + rfo.getRfoId() + 
                             (rfo.getNucleus() != null ? " (asociado a Nucleus ID: " + rfo.getNucleus().getId() + ")" : " (sin asociación)"));
            return true;
            
        } catch (DataIntegrityViolationException e) {
            System.err.println("ERROR: Violación de integridad para RFO ID " + rfoDTO.getRfoId() + ": " + e.getMessage() + ". Registro #" + processedCount);
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: " + e.getMessage() + ". Registro #" + processedCount);
            return false;
        } catch (Exception e) {
            System.err.println("ERROR: Error inesperado procesando RFO ID " + rfoDTO.getRfoId() + ": " + e.getMessage() + ". Registro #" + processedCount);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Validate that the Rfo has required fields and valid associations
     */
    private boolean isValidRfo(Rfo rfo) {
        System.out.println("DEBUG: Validando RFO...");
        
        if (rfo.getRfoId() == null) {
            System.err.println("❌ RFO ID es obligatorio");
            return false;
        }
        
        // NUEVA VALIDACIÓN: Verificar que tenga asociación con Nucleus (requerido por regla de negocio)
        if (rfo.getNucleus() == null) {
            System.err.println("❌ ERROR: RFO debe tener una asociación válida con Nucleus (SN2 debe existir en nucleus_services)");
            return false;
        }
        
        System.out.println("✅ DEBUG: RFO válido con asociación a Nucleus ID: " + rfo.getNucleus().getId());
        return true;
    }
    
    /**
     * Compara dos estados ignorando mayúsculas, acentos y espacios
     */
    private boolean estadosIguales(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        String normA = normalizarEstado(a);
        String normB = normalizarEstado(b);
        return normA.equals(normB);
    }
    
    /**
     * Normaliza un estado removiendo acentos, espacios extra y convirtiendo a mayúsculas
     */
    private String normalizarEstado(String s) {
        if (s == null) return "";
        return java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .replaceAll("\\s+", " ")
            .trim()
            .toUpperCase();
    }
}