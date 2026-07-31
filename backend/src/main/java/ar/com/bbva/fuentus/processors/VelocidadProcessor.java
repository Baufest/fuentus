package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.VelocidadImportDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Velocidad;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.VelocidadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Component
public class VelocidadProcessor implements EntityProcessor {

    private static final Logger logger = LoggerFactory.getLogger(VelocidadProcessor.class);

    @Autowired
    private VelocidadRepository velocidadRepository;

    @Autowired
    private NucleusRepository nucleusRepository;

    // Formateadores para diferentes formatos de fecha
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        createSpanishMonthFormatter("d "),
        createSpanishMonthFormatter("dd "),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("d-MMM-yyyy", new Locale("es", "ES")),
        DateTimeFormatter.ofPattern("dd-MMM-yyyy", new Locale("es", "ES"))
    };

    private static DateTimeFormatter createSpanishMonthFormatter(String dayPattern) {
        Map<Long, String> monthNames = new HashMap<>();
        monthNames.put(1L, "ene");
        monthNames.put(2L, "feb");
        monthNames.put(3L, "mar");
        monthNames.put(4L, "abr");
        monthNames.put(5L, "may");
        monthNames.put(6L, "jun");
        monthNames.put(7L, "jul");
        monthNames.put(8L, "ago");
        monthNames.put(9L, "sep");
        monthNames.put(10L, "oct");
        monthNames.put(11L, "nov");
        monthNames.put(12L, "dic");
        
        return new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern(dayPattern)
            .appendText(ChronoField.MONTH_OF_YEAR, monthNames)
            .appendPattern(" yyyy")
            .toFormatter(Locale.forLanguageTag("es"));
    }

    @Override
    public boolean process(Object dto) {
        if (!(dto instanceof VelocidadImportDTO)) {
            throw new IllegalArgumentException("Expected VelocidadImportDTO but got: " +
                    (dto != null ? dto.getClass().getSimpleName() : "null"));
        }

        VelocidadImportDTO importDTO = (VelocidadImportDTO) dto;

        try {
            Velocidad velocidad = new Velocidad();

            // Buscar el Nucleus por Service_N2
            Nucleus nucleus = null;
            if (importDTO.getServiceN2() != null && !importDTO.getServiceN2().trim().isEmpty()) {
                Optional<Nucleus> nucleusOpt = nucleusRepository.findByServiceN2(importDTO.getServiceN2().trim());
                if (nucleusOpt.isPresent()) {
                    nucleus = nucleusOpt.get();
                    logger.debug("Nucleus encontrado para Service_N2: {}", importDTO.getServiceN2());
                } else {
                    logger.warn("No se encontró Nucleus con Service_N2: {}", importDTO.getServiceN2());
                }
            }
            velocidad.setNucleus(nucleus);

            // Setear LT y CT
            velocidad.setLt(parseInteger(importDTO.getLt()));
            velocidad.setCt(parseInteger(importDTO.getCt()));

            // Encontrar la fecha más reciente entre todas las columnas *_date
            LocalDate mostRecentDate = findMostRecentDate(importDTO);
            if (mostRecentDate == null) {
                logger.error("No se pudo determinar una fecha válida para el registro");
                return false;
            }
            velocidad.setDate(mostRecentDate);

            velocidadRepository.save(velocidad);
            logger.debug("Velocidad procesada exitosamente para Service_N2: {}", importDTO.getServiceN2());
            return true;

        } catch (Exception e) {
            logger.error("Error procesando velocidad: {}", e.getMessage(), e);
            return false;
        }
    }

    private LocalDate findMostRecentDate(VelocidadImportDTO dto) {
        List<LocalDate> dates = new ArrayList<>();

        // Intentar parsear todas las fechas
        addDateIfValid(dates, dto.getNewDate());
        addDateIfValid(dates, dto.getAnalyzingDate());
        addDateIfValid(dates, dto.getReadyDate());
        addDateIfValid(dates, dto.getInProgressDate());
        addDateIfValid(dates, dto.getTestDate());
        addDateIfValid(dates, dto.getReadyToVerifyDate());
        addDateIfValid(dates, dto.getToReworkDate());
        addDateIfValid(dates, dto.getBlockedDate());
        addDateIfValid(dates, dto.getAcceptedDate());
        addDateIfValid(dates, dto.getDiscardedDate());
        addDateIfValid(dates, dto.getReadyToDeployDate());
        addDateIfValid(dates, dto.getDeployedDate());

        // Retornar la fecha más reciente
        return dates.stream()
                .max(LocalDate::compareTo)
                .orElse(null);
    }

    private void addDateIfValid(List<LocalDate> dates, String dateStr) {
        LocalDate date = parseDate(dateStr);
        if (date != null) {
            dates.add(date);
        }
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        String trimmed = dateStr.trim();

        // Intentar con cada formateador
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(trimmed, formatter);
            } catch (DateTimeParseException e) {
                // Continuar con el siguiente formato
            }
        }

        logger.debug("No se pudo parsear la fecha: {}", dateStr);
        return null;
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equals("#N/A")) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("No se pudo parsear el valor entero: {}", value);
            return null;
        }
    }
}
