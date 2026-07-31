package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.dto.ProductividadImportDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Productividad;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.ProductividadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class ProductividadProcessor implements EntityProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ProductividadProcessor.class);

    @Autowired
    private ProductividadRepository productividadRepository;

    @Autowired
    private NucleusRepository nucleusRepository;

    private LocalDate fecha; // La fecha se seteará antes de procesar

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean process(Object dto) {
        if (!(dto instanceof ProductividadImportDTO)) {
            throw new IllegalArgumentException("Expected ProductividadImportDTO but got: " +
                    (dto != null ? dto.getClass().getSimpleName() : "null"));
        }

        ProductividadImportDTO importDTO = (ProductividadImportDTO) dto;

        try {
            Productividad productividad = new Productividad();

            // Buscar el Nucleus por Service_N2
            Nucleus nucleus = null;
            if (importDTO.getServicioN2() != null && !importDTO.getServicioN2().trim().isEmpty()) {
                Optional<Nucleus> nucleusOpt = nucleusRepository.findByServiceN2(importDTO.getServicioN2().trim());
                if (nucleusOpt.isPresent()) {
                    nucleus = nucleusOpt.get();
                    logger.debug("Nucleus encontrado para Service_N2: {}", importDTO.getServicioN2());
                } else {
                    logger.warn("No se encontró Nucleus con Service_N2: {}", importDTO.getServicioN2());
                }
            }
            productividad.setNucleus(nucleus); // Puede ser null

            // Setear features (puede ser #N/A)
            productividad.setFeatures(parseInteger(importDTO.getFeatures()));

            // Parsear FTEs (vienen con coma como separador decimal)
            productividad.setFtesDirectos(parseDecimalWithComma(importDTO.getFtesDirectos()));
            productividad.setFtesIndirectos(parseDecimalWithComma(importDTO.getFtesIndirectos()));

            // Setear fecha
            if (this.fecha == null) {
                logger.error("Fecha no establecida para el processor");
                return false;
            }
            productividad.setFecha(this.fecha);

            productividadRepository.save(productividad);
            logger.debug("Productividad procesada exitosamente para Service_N2: {}", importDTO.getServicioN2());
            return true;

        } catch (Exception e) {
            logger.error("Error procesando productividad: {}", e.getMessage(), e);
            return false;
        }
    }

    private Double parseDecimalWithComma(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equals("#N/A")) {
            return 0.0;
        }
        try {
            String normalized = value.trim().replace(",", ".");
            return Double.parseDouble(normalized);
        } catch (NumberFormatException e) {
            logger.warn("No se pudo parsear el valor decimal: {}", value);
            return 0.0;
        }
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
