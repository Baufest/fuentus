package ar.com.bbva.fuentus.processors;

/**
 * Interface for processing DTOs and persisting them to database
 */
public interface EntityProcessor {
    /**
     * Process a DTO and persist it to the database
     * @param dto The DTO to process
     * @return true if processing was successful, false otherwise
     */
    boolean process(Object dto);
}