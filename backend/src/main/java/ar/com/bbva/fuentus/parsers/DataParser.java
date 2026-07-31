package ar.com.bbva.fuentus.parsers;

import ar.com.bbva.fuentus.enums.EntityType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Interface for parsing different file formats into DTOs
 */
public interface DataParser {
    /**
     * Parse a file into a list of DTOs based on the entity type
     * @param file The file to parse
     * @param entityType The type of entity to parse into
     * @return List of parsed DTOs
     * @throws Exception if parsing fails
     */
    List<?> parse(MultipartFile file, EntityType entityType) throws Exception;
}