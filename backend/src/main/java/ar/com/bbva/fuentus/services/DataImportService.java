package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.ImportResultDTO;
import ar.com.bbva.fuentus.dto.NucleusImportDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.enums.EntityType;
import ar.com.bbva.fuentus.enums.FileType;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class DataImportService {

    @Autowired
    private NucleusRepository nucleusRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public ImportResultDTO importData(MultipartFile file, EntityType entityType, FileType fileType) throws Exception {
        
        System.out.println("=== INICIO IMPORT ===");
        System.out.println("Entity Type: " + entityType);
        System.out.println("File Type: " + fileType);
        
        if (entityType != EntityType.NUCLEUS_SERVICES) {
            throw new IllegalArgumentException("Solo NUCLEUS_SERVICES está implementado");
        }
        
        // Ahora soportamos JSON y CSV
        if (fileType != FileType.JSON && fileType != FileType.CSV) {
            throw new IllegalArgumentException("Solo JSON y CSV están implementados");
        }
        
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        try {
            List<NucleusImportDTO> dtos;
            
            // Parsear según el tipo de archivo
            if (fileType == FileType.JSON) {
                // Parsear JSON
                dtos = objectMapper.readValue(
                    file.getInputStream(), 
                    new TypeReference<List<NucleusImportDTO>>() {}
                );
            } else {
                // Parsear CSV
                dtos = parseCsvFile(file);
            }
            
            System.out.println("DTOs parseados: " + dtos.size());
            
            if (dtos.isEmpty()) {
                throw new IllegalArgumentException("No se encontraron datos en el archivo");
            }
            
            return processImportData(dtos);
            
        } catch (Exception e) {
            System.err.println("ERROR GENERAL EN IMPORT: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error procesando archivo: " + e.getMessage(), e);
        }
    }
    
    private List<NucleusImportDTO> parseCsvFile(MultipartFile file) throws Exception {
        System.out.println("=== PARSEANDO CSV ===");
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<NucleusImportDTO> csvToBean = new CsvToBeanBuilder<NucleusImportDTO>(reader)
                .withType(NucleusImportDTO.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
            
            List<NucleusImportDTO> dtos = csvToBean.parse();
            System.out.println("CSV parseado exitosamente: " + dtos.size() + " registros");
            return dtos;
            
        } catch (Exception e) {
            System.err.println("Error parseando CSV: " + e.getMessage());
            throw new RuntimeException("Error parseando archivo CSV: " + e.getMessage(), e);
        }
    }
    
    private ImportResultDTO processImportData(List<NucleusImportDTO> dtos) {
        int totalProcessed = dtos.size();
        int successfulImports = 0;
        
        for (int i = 0; i < dtos.size(); i++) {
            NucleusImportDTO dto = dtos.get(i);
            
            try {
                System.out.println("--- Procesando DTO " + (i + 1) + " ---");
                System.out.println("ServiceN1: " + dto.getServiceN1());
                System.out.println("ServiceN2: " + dto.getServiceN2());
                
                // Validar campos obligatorios
                if (dto.getServiceN1() == null || dto.getServiceN1().trim().isEmpty()) {
                    System.err.println("ServiceN1 vacío, saltando...");
                    continue;
                }
                
                if (dto.getServiceN2() == null || dto.getServiceN2().trim().isEmpty()) {
                    System.err.println("ServiceN2 vacío, saltando...");
                    continue;
                }
                
                // Buscar existente usando query nativa para evitar problemas de contexto
                @SuppressWarnings("unchecked")
                List<Nucleus> existingList = entityManager.createNativeQuery(
                    "SELECT * FROM nucleus_services WHERE Service_N1 = ?1 AND Service_N2 = ?2", 
                    Nucleus.class)
                    .setParameter(1, dto.getServiceN1())
                    .setParameter(2, dto.getServiceN2())
                    .getResultList();
                
                Nucleus nucleus;
                
                if (!existingList.isEmpty()) {
                    // UPDATE: usar la entidad existente
                    nucleus = existingList.get(0);
                    System.out.println("Actualizando registro existente con ID: " + nucleus.getId());
                    
                    // Actualizar campos
                    updateNucleusFromDTO(dto, nucleus);
                    
                } else {
                    // INSERT: crear nueva entidad
                    System.out.println("Creando nuevo registro");
                    nucleus = new Nucleus();
                    
                    // Mapear todos los campos
                    updateNucleusFromDTO(dto, nucleus);
                }
                
                // Verificar que la entidad está en estado válido antes de guardar
                System.out.println("Nucleus antes de save - ID: " + nucleus.getId());
                
                // Guardar
                Nucleus savedNucleus = nucleusRepository.saveAndFlush(nucleus);
                
                System.out.println("Guardado exitoso con ID: " + savedNucleus.getId());
                successfulImports++;
                
                // Limpiar contexto para evitar problemas
                entityManager.clear();
                
            } catch (Exception e) {
                System.err.println("Error procesando DTO " + (i + 1) + ": " + e.getMessage());
                e.printStackTrace();
                
                // Limpiar contexto en caso de error
                entityManager.clear();
                
                // Continuar con el siguiente
            }
        }
        
        System.out.println("=== FIN IMPORT ===");
        System.out.println("Total procesados: " + totalProcessed);
        System.out.println("Exitosos: " + successfulImports);
        
        return new ImportResultDTO(totalProcessed, successfulImports, null);
    }
    
    private void updateNucleusFromDTO(NucleusImportDTO dto, Nucleus nucleus) {
        nucleus.setServiceN1(dto.getServiceN1());
        nucleus.setServiceN2(dto.getServiceN2());
        nucleus.setServiceN2description(dto.getServiceN2description());
        nucleus.setArea(dto.getArea());
        nucleus.setUuaa(dto.getUuaa());
        nucleus.setEstado(dto.getEstado());
        nucleus.setOwnerIdServiceN1(dto.getOwnerIdServiceN1());
        nucleus.setOwnerServiceN1(dto.getOwnerServiceN1());
        nucleus.setOwnerIdServiceN2(dto.getOwnerIdServiceN2());
        nucleus.setOwnerServiceN2(dto.getOwnerServiceN2());
        nucleus.setOrgN1(dto.getOrgN1());
        nucleus.setOwnerIdOrgN1(dto.getOwnerIdOrgN1());
        nucleus.setOwnerOrgN1(dto.getOwnerOrgN1());
        nucleus.setOrgN2fabrica(dto.getOrgN2fabrica());
        nucleus.setOwnerIdOrgN2(dto.getOwnerIdOrgN2());
        nucleus.setOwnerOrg2ftl(dto.getOwnerOrg2ftl());
        nucleus.setCfs(dto.getCfs());
        nucleus.setSaas(dto.getSaas());
        nucleus.setDisponibilidadCnegocio(dto.getDisponibilidadCnegocio());
        nucleus.setConfidencialidadIcc(dto.getConfidencialidadIcc());
        nucleus.setIntegridad(dto.getIntegridad());
        nucleus.setAutenticidad(dto.getAutenticidad());
        nucleus.setRelevanteResolucion(dto.getRelevanteResolucion());
        nucleus.setCategoria(dto.getCategoria());
        nucleus.setDescGlobalRels(dto.getDescGlobalRels());
        nucleus.setAscGlobalRels(dto.getAscGlobalRels());
        nucleus.setDescRegRels(dto.getDescRegRels());
        nucleus.setAscRegRels(dto.getAscRegRels());
        nucleus.setRelType(dto.getRelType());
    }

    public EntityType[] getSupportedEntityTypes() {
        return EntityType.values();
    }

    public FileType[] getSupportedFileTypes() {
        return FileType.values();
    }
}