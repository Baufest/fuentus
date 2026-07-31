package ar.com.bbva.fuentus.parsers;

import ar.com.bbva.fuentus.dto.*;
import ar.com.bbva.fuentus.enums.EntityType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvParser implements DataParser {

    @Override
    public List<?> parse(MultipartFile file, EntityType entityType) throws Exception {
        List<Object> result = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                Object dto = parseLineToDTO(line, entityType);
                if (dto != null) {
                    result.add(dto);
                }
            }
        }
        
        return result;
    }

    private Object parseLineToDTO(String line, EntityType entityType) {
        String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        
        switch (entityType) {
            case NUCLEUS_SERVICES:
                return parseToNucleusDTO(fields);
            case CHIMERA_SAST:
                // TODO: Implementar cuando se tenga ChimeraSastImportDTO
                throw new UnsupportedOperationException("ChimeraSast CSV parsing not implemented yet");
            case APPS:
                // TODO: Implementar cuando se tenga AppsImportDTO
                throw new UnsupportedOperationException("Apps CSV parsing not implemented yet");
            default:
                throw new IllegalArgumentException("Unsupported entity type for CSV: " + entityType);
        }
    }

    private NucleusImportDTO parseToNucleusDTO(String[] fields) {
        NucleusImportDTO dto = new NucleusImportDTO();
        try {
            dto.setServiceN1(getFieldValue(fields, 0));
            dto.setOwnerIdServiceN1(getFieldValue(fields, 1));
            dto.setOwnerServiceN1(getFieldValue(fields, 2));
            dto.setServiceN2(getFieldValue(fields, 3));
            dto.setServiceN2description(getFieldValue(fields, 4));
            dto.setOwnerIdServiceN2(getFieldValue(fields, 5));
            dto.setOwnerServiceN2(getFieldValue(fields, 6));
            dto.setArea(getFieldValue(fields, 7));
            dto.setOrgN1(getFieldValue(fields, 8));
            dto.setOwnerIdOrgN1(getFieldValue(fields, 9));
            dto.setOwnerOrgN1(getFieldValue(fields, 10));
            dto.setOrgN2fabrica(getFieldValue(fields, 11));
            dto.setOwnerIdOrgN2(getFieldValue(fields, 12));
            dto.setOwnerOrg2ftl(getFieldValue(fields, 13));
            dto.setCfs(getFieldValue(fields, 14));
            dto.setSaas(getFieldValue(fields, 15));
            dto.setDisponibilidadCnegocio(getFieldValue(fields, 16));
            dto.setConfidencialidadIcc(getFieldValue(fields, 17));
            dto.setIntegridad(getFieldValue(fields, 18));
            dto.setAutenticidad(getFieldValue(fields, 19));
            dto.setRelevanteResolucion(getFieldValue(fields, 20));
            dto.setCategoria(getFieldValue(fields, 21));
            dto.setUuaa(getFieldValue(fields, 22));
            dto.setEstado(getFieldValue(fields, 23));
            
            String descGlobalRelsStr = getFieldValue(fields, 24);
            if (descGlobalRelsStr != null && !descGlobalRelsStr.isEmpty()) {
                try {
                    dto.setDescGlobalRels(Integer.parseInt(descGlobalRelsStr));
                } catch (NumberFormatException e) {
                    // Log warning but continue processing
                    System.err.println("Warning: Invalid number format for descGlobalRels: " + descGlobalRelsStr);
                }
            }
            
            dto.setAscGlobalRels(getFieldValue(fields, 25));
            dto.setDescRegRels(getFieldValue(fields, 26));
            dto.setAscRegRels(getFieldValue(fields, 27));
            dto.setRelType(getFieldValue(fields, 28));
            
        } catch (Exception e) {
            throw new RuntimeException("Error parsing CSV line for Nucleus: " + String.join(",", fields), e);
        }
        
        return dto;
    }

    private String getFieldValue(String[] fields, int index) {
        if (fields.length > index && fields[index] != null) {
            String value = fields[index].trim();
            // Remove quotes if present
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            return value.isEmpty() ? null : value;
        }
        return null;
    }
}