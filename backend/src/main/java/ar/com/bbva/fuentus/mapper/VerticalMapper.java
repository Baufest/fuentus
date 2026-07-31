package ar.com.bbva.fuentus.mapper;

import ar.com.bbva.fuentus.dto.VerticalResponseDto;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Vertical;
import ar.com.bbva.fuentus.entities.VerticalNucleus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class VerticalMapper {
    

    
    /**
     * Mapea entidad Vertical a DTO sin orgN2fabrica
     */
    public VerticalResponseDto mapToResponseDto(Vertical vertical) {
        if (vertical == null) {
            return null;
        }

        VerticalResponseDto dto = new VerticalResponseDto();
        dto.setId(vertical.getId());
        dto.setName(vertical.getName());
        dto.setRefVertical(vertical.getRefVertical());
        return dto;
    }
    
    /**
     * Mapea entidad Vertical a DTO con orgN2fabrica distintos
     */
    public VerticalResponseDto mapToResponseDtoWithOrgN2Fabrica(Vertical vertical) {
        if (vertical == null) {
            return null;
        }
        
        VerticalResponseDto dto = mapToResponseDto(vertical);
        
        // Obtener los orgN2fabrica distintos para esta vertical
        List<VerticalResponseDto.FactoryDTO> orgN2fabricaList = vertical.getVerticalNucleusList().stream()
                .map((verticalNucleus) -> {
                    VerticalResponseDto.FactoryDTO factoryDTO = new VerticalResponseDto.FactoryDTO();
                    Nucleus nucleus = verticalNucleus.getNucleus();
                    if (nucleus != null) {
                        factoryDTO.setName(nucleus.getOrgN2fabrica());
                        factoryDTO.setOwnerFactory(nucleus.getOwnerOrg2ftl());
                        return factoryDTO;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        
        dto.setOrgN2fabrica(orgN2fabricaList);
        
        return dto;
    }

}