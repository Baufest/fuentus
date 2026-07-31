package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.ComboValuesDTO;
import ar.com.bbva.fuentus.dto.NucleusGetRequestDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.mapper.NucleusMapper;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.VerticalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NucleusService {

    @Autowired
    private NucleusRepository nucleusRepository;

    @Autowired
    private VerticalRepository verticalRepository;

    public NucleusGetRequestDTO getServiceById(Long id) {
        return nucleusRepository.findById(id)
                .map(NucleusMapper::mapNucleus2GetRequestDTO)
                .orElse(null);
    }

    public List<NucleusGetRequestDTO> getAppsByUUAA(String uuaa) {
        List<Nucleus> listaNucleusApps = nucleusRepository.findByUuaaLike("%" + uuaa + "%");

        List<NucleusGetRequestDTO> listaNucleusAppsDTO = new ArrayList<>();
        listaNucleusAppsDTO = listaNucleusApps.stream()
                .map(NucleusMapper::mapNucleus2GetRequestDTO)
                .collect(Collectors.toList());

        return listaNucleusAppsDTO;
    }

    public Page<NucleusGetRequestDTO> findByAnyField(String searchTerm, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Nucleus> nucleusPage = nucleusRepository.findByAnyFieldContainingPageable(searchTerm, pageable);
        return nucleusPage.map(NucleusMapper::mapNucleus2GetRequestDTO);
    }

    public Page<NucleusGetRequestDTO> searchWithFilters(String searchTerm, String vertical, String uol2, String sn1, String sn2, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        
        // Si hay filtros aplicados, usar el método con filtros
        boolean hasFilters = (vertical != null && !vertical.trim().isEmpty()) ||
                            (uol2 != null && !uol2.trim().isEmpty()) ||
                            (sn1 != null && !sn1.trim().isEmpty()) ||
                            (sn2 != null && !sn2.trim().isEmpty());
        
        if (hasFilters) {
            // Usar searchTerm como filtro de uuaa si está presente
            String uuaaFilter = (searchTerm != null && !searchTerm.trim().isEmpty()) ? searchTerm : null;
            Page<Nucleus> nucleusPage = nucleusRepository.findByFilterFieldsPageable(vertical, uol2, sn1, sn2, uuaaFilter, pageable);
            return nucleusPage.map(NucleusMapper::mapNucleus2GetRequestDTO);
        } else {
            // Si no hay filtros, usar la búsqueda general
            Page<Nucleus> nucleusPage = nucleusRepository.findByAnyFieldContainingPageable(searchTerm, pageable);
            return nucleusPage.map(NucleusMapper::mapNucleus2GetRequestDTO);
        }
    }

    public Page<NucleusGetRequestDTO> getNucleusByOptionalFields(String area, String orgN1, String orgN2, int page) {
        System.out.println("Buscando con parámetros - area: " + area + ", orgN1: " + orgN1 + ", orgN2: " + orgN2);
        Pageable pageable = PageRequest.of(page, 10);
        Page<Nucleus> nucleusPage = nucleusRepository.findByOptionalFieldsPageable(area, orgN1, orgN2, pageable);
        System.out.println("Encontrados " + nucleusPage.getTotalElements() + " resultados");
        return nucleusPage.map(NucleusMapper::mapNucleus2GetRequestDTO);
    }

    /**
     * Obtiene todos los valores únicos para los combos con filtros encadenados.
     * Cada filtro limita las opciones de los filtros siguientes:
     * vertical -> uol2 -> sn1 -> sn2
     */
    public ComboValuesDTO getComboValues(String vertical, String uol2, String sn1) {
        // Verticales siempre muestra todas las opciones
        List<String> verticals = verticalRepository.findDistinctVerticalNames();
        
        // UOL2 se filtra por vertical (si está seleccionada)
        List<String> uol2Values = nucleusRepository.findDistinctUol2ValuesWithFilters(vertical);
        
        // SN1 se filtra por vertical y uol2
        List<String> sn1Values = nucleusRepository.findDistinctSn1ValuesWithFilters(vertical, uol2);
        
        // SN2 se filtra por vertical, uol2 y sn1
        List<String> sn2Values = nucleusRepository.findDistinctSn2ValuesWithFilters(vertical, uol2, sn1);

        return new ComboValuesDTO(verticals, uol2Values, sn1Values, sn2Values);
    }

}
