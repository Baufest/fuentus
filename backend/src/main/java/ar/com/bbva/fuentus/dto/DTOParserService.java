package ar.com.bbva.fuentus.dto;

import ar.com.bbva.fuentus.enums.EntityType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class DTOParserService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public List<?> parseCsvFile(MultipartFile file, EntityType entityType) throws Exception {
        Class<?> dtoClass = getDTOClass(entityType);
        char separator = entityType == EntityType.NUCLEUS_SERVICES ? ';' : ',';
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<?> csvToBean = new CsvToBeanBuilder<>(reader)
                .withType(dtoClass)
                .withSeparator(separator)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
            
            List<?> dtos = csvToBean.parse();
            System.out.println("CSV parseado exitosamente: " + dtos.size() + " registros para " + entityType);
            return dtos;
            
        } catch (Exception e) {
            System.err.println("Error parseando CSV para " + entityType + ": " + e.getMessage());
            throw new RuntimeException("Error parseando archivo CSV: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<?> parseJsonFile(MultipartFile file, EntityType entityType) throws Exception {
        TypeReference<?> typeRef = getTypeReference(entityType);
        
        try {
            List<?> dtos = (List<?>) objectMapper.readValue(file.getInputStream(), typeRef);
            System.out.println("JSON parseado exitosamente: " + dtos.size() + " registros para " + entityType);
            return dtos;
            
        } catch (Exception e) {
            System.err.println("Error parseando JSON para " + entityType + ": " + e.getMessage());
            throw new RuntimeException("Error parseando archivo JSON: " + e.getMessage(), e);
        }
    }

    private Class<?> getDTOClass(EntityType entityType) {
        switch (entityType) {
            case NUCLEUS_SERVICES:
                return NucleusImportDTO.class;
            case CHIMERA_SCA:
                return ChimeraScaImportDTO.class;
            case CHIMERA_SAST:
                return ChimeraSastImportDTO.class;
            case APPS:
                return AppImportDTO.class;
            case RFO:
                return RfoImportDTO.class;
            default:
                throw new IllegalArgumentException("No DTO class found for entity type: " + entityType);
        }
    }

    private TypeReference<?> getTypeReference(EntityType entityType) {
        switch (entityType) {
            case NUCLEUS_SERVICES:
                return new TypeReference<List<NucleusImportDTO>>() {};
            case CHIMERA_SCA:
                return new TypeReference<List<ChimeraScaImportDTO>>() {};
            case CHIMERA_SAST:
                return new TypeReference<List<ChimeraSastImportDTO>>() {};
            case APPS:
                return new TypeReference<List<AppImportDTO>>() {};
            case RFO:
                return new TypeReference<List<RfoImportDTO>>() {};
            default:
                throw new IllegalArgumentException("No TypeReference found for entity type: " + entityType);
        }
    }
}