package ar.com.bbva.fuentus.config;

import ar.com.bbva.fuentus.enums.*;
import ar.com.bbva.fuentus.parsers.*;
import ar.com.bbva.fuentus.processors.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataImportConfig {

    /**
     * Configure the mapping between file types and their respective parsers
     */
    @Bean
    public Map<FileType, DataParser> dataParsers(CsvParser csvParser, JsonParser jsonParser) {
        Map<FileType, DataParser> parsers = new HashMap<>();
        parsers.put(FileType.CSV, csvParser);
        parsers.put(FileType.JSON, jsonParser);
        return parsers;
    }

    /**
     * Configure the mapping between entity types and their respective processors
     */
    @Bean
    public Map<EntityType, EntityProcessor> entityProcessors(NucleusProcessor nucleusProcessor) {
        Map<EntityType, EntityProcessor> processors = new HashMap<>();
        processors.put(EntityType.NUCLEUS_SERVICES, nucleusProcessor);
        // TODO: Add more processors when they are implemented
        // processors.put(EntityType.CHIMERA_SAST, chimeraSastProcessor);
        // processors.put(EntityType.APPS, appsProcessor);
        return processors;
    }
}