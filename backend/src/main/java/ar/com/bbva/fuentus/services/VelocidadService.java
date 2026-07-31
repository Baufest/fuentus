package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.VelocidadDTO;
import ar.com.bbva.fuentus.dto.VelocidadImportDTO;
import ar.com.bbva.fuentus.entities.Velocidad;
import ar.com.bbva.fuentus.processors.VelocidadProcessor;
import ar.com.bbva.fuentus.repositories.VelocidadRepository;
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
public class VelocidadService {

    private static final Logger logger = LoggerFactory.getLogger(VelocidadService.class);

    @Autowired
    private VelocidadRepository velocidadRepository;

    @Autowired
    private VelocidadProcessor velocidadProcessor;

    @Transactional
    public List<String> importFromCSV(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<VelocidadImportDTO> importList = new CsvToBeanBuilder<VelocidadImportDTO>(reader)
                    .withType(VelocidadImportDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();

            logger.info("Procesando {} registros de velocidad", importList.size());

            for (int i = 0; i < importList.size(); i++) {
                VelocidadImportDTO dto = importList.get(i);
                try {
                    boolean success = velocidadProcessor.process(dto);
                    if (success) {
                        successCount++;
                    } else {
                        errorCount++;
                        String error = String.format("Error en fila %d: %s", i + 2, dto.getServiceN2());
                        logger.error(error);
                        errors.add(error);
                    }
                } catch (Exception e) {
                    errorCount++;
                    String error = String.format("Error en fila %d: %s - %s", i + 2, dto.getServiceN2(), e.getMessage());
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

    public List<VelocidadDTO> getAllByDate(LocalDate date) {
        List<Velocidad> list = velocidadRepository.findByDate(date);
        return list.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VelocidadDTO> getAll() {
        List<Velocidad> list = velocidadRepository.findAll();
        return list.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VelocidadDTO> getByNucleusId(Long nucleusId) {
        List<Velocidad> list = velocidadRepository.findByNucleusId(nucleusId);
        return list.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private VelocidadDTO convertToDTO(Velocidad entity) {
        VelocidadDTO dto = new VelocidadDTO();
        dto.setId(entity.getId());
        dto.setNucleusId(entity.getNucleus() != null ? entity.getNucleus().getId() : null);
        dto.setServicioN2(entity.getNucleus() != null ? entity.getNucleus().getServiceN2() : null);
        dto.setLt(entity.getLt());
        dto.setCt(entity.getCt());
        dto.setDate(entity.getDate());
        return dto;
    }
}
