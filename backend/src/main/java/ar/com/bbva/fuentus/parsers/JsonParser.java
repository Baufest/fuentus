package ar.com.bbva.fuentus.parsers;

import ar.com.bbva.fuentus.dto.*;
import ar.com.bbva.fuentus.enums.EntityType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class JsonParser implements DataParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<?> parse(MultipartFile file, EntityType entityType) throws Exception {
        switch (entityType) {
            case NUCLEUS_SERVICES:
                return objectMapper.readValue(file.getInputStream(), 
                    new TypeReference<List<NucleusImportDTO>>() {});
            case CHIMERA_SCA:
                return objectMapper.readValue(file.getInputStream(), 
                    new TypeReference<List<ChimeraScaImportDTO>>() {});
            case CHIMERA_SAST:
                // TODO: Implementar cuando se tenga ChimeraSastImportDTO
                throw new UnsupportedOperationException("ChimeraSast JSON parsing not implemented yet");
            case APPS:
                // TODO: Implementar cuando se tenga AppsImportDTO
                throw new UnsupportedOperationException("Apps JSON parsing not implemented yet");
            default:
                throw new IllegalArgumentException("Unsupported entity type for JSON: " + entityType);
        }
    }
}