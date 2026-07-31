package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.ProductividadDTO;
import ar.com.bbva.fuentus.dto.ProductividadImportDTO;
import ar.com.bbva.fuentus.entities.Productividad;
import ar.com.bbva.fuentus.processors.ProductividadProcessor;
import ar.com.bbva.fuentus.repositories.ProductividadRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductividadService {

    private static final Logger logger = LoggerFactory.getLogger(ProductividadService.class);

    @Autowired
    private ProductividadRepository productividadRepository;

    @Autowired
    private ProductividadProcessor productividadProcessor;

    @Transactional
    public List<String> importFromCSV(MultipartFile file, LocalDate fecha) {
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<ProductividadImportDTO> importList = new CsvToBeanBuilder<ProductividadImportDTO>(reader)
                    .withType(ProductividadImportDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();

            logger.info("Procesando {} registros de productividad para la fecha {}", importList.size(), fecha);

            // Setear la fecha en el processor
            productividadProcessor.setFecha(fecha);

            for (int i = 0; i < importList.size(); i++) {
                ProductividadImportDTO dto = importList.get(i);
                try {
                    boolean success = productividadProcessor.process(dto);
                    if (success) {
                        successCount++;
                    } else {
                        errorCount++;
                        String error = String.format("Error en fila %d: %s", i + 2, dto.getServicioN2());
                        logger.error(error);
                        errors.add(error);
                    }
                } catch (Exception e) {
                    errorCount++;
                    String error = String.format("Error en fila %d: %s - %s", i + 2, dto.getServicioN2(), e.getMessage());
                    logger.error(error, e);
                    errors.add(error);
                }
            }

            logger.info("Importación completada: {} exitosos, {} errores", successCount, errorCount);

            if (errors.isEmpty()) {
                errors.add(String.format("Importación exitosa: %d registros procesados", successCount));
            } else {
                errors.add(0, String.format("Importación parcial: %d exitosos, %d errores", successCount, errorCount));
            }

        } catch (Exception e) {
            logger.error("Error al procesar el archivo CSV", e);
            errors.add("Error al procesar el archivo: " + e.getMessage());
        }

        return errors;
    }

    public List<ProductividadDTO> getAllByFecha(LocalDate fecha) {
        List<Productividad> list = productividadRepository.findByFecha(fecha);
        return list.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductividadDTO> getAll() {
        List<Productividad> list = productividadRepository.findAll();
        return list.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductividadDTO> getByNucleusId(Long nucleusId) {
        List<Productividad> list = productividadRepository.findByNucleusId(nucleusId);
        return list.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductividadDTO convertToDTO(Productividad entity) {
        ProductividadDTO dto = new ProductividadDTO();
        dto.setId(entity.getId());
        dto.setNucleusId(entity.getNucleus() != null ? entity.getNucleus().getId() : null);
        dto.setServicioN2(entity.getNucleus() != null ? entity.getNucleus().getServiceN2() : null);
        dto.setFeatures(entity.getFeatures());
        dto.setFtesDirectos(entity.getFtesDirectos());
        dto.setFtesIndirectos(entity.getFtesIndirectos());
        dto.setFecha(entity.getFecha());
        return dto;
    }
}
