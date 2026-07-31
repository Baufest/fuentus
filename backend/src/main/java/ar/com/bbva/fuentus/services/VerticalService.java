package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.FactoryVertical;
import ar.com.bbva.fuentus.dto.OrgN2FabricaResponseDto;
import ar.com.bbva.fuentus.dto.VerticalRequestDto;
import ar.com.bbva.fuentus.dto.VerticalResponseDto;
import ar.com.bbva.fuentus.dto.VerticalUpdateDto;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Vertical;
import ar.com.bbva.fuentus.entities.VerticalNucleus;
import ar.com.bbva.fuentus.mapper.VerticalMapper;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.VerticalNucleusRepository;
import ar.com.bbva.fuentus.repositories.VerticalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class VerticalService {
    
    private final VerticalRepository verticalRepository;
    private final VerticalMapper verticalMapper;
    private final VerticalNucleusRepository verticalNucleusRepository;
    private final NucleusRepository nucleusRepository;
    
    public VerticalService(VerticalRepository verticalRepository, 
                          VerticalMapper verticalMapper,
                          VerticalNucleusRepository verticalNucleusRepository,
                          NucleusRepository nucleusRepository) {
        this.verticalRepository = verticalRepository;
        this.verticalMapper = verticalMapper;
        this.verticalNucleusRepository = verticalNucleusRepository;
        this.nucleusRepository = nucleusRepository;
    }
    
    /**
     * Crea una nueva vertical
     */
    public VerticalResponseDto createVertical(VerticalRequestDto verticalRequestDto) {
        if (verticalRequestDto.getName() == null || verticalRequestDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        Vertical vertical = new Vertical();
        vertical.setName(verticalRequestDto.getName().toUpperCase().trim());
        vertical.setRefVertical(verticalRequestDto.getRefVertical().toUpperCase().trim());

        Vertical savedVertical = verticalRepository.save(vertical);
        updateFactories(verticalRequestDto.getOrgN2fabricaList(), savedVertical);

        return verticalMapper.mapToResponseDto(savedVertical);
    }
    
    /**
     * Obtiene todas las verticales con sus orgN2fabrica distintos
     */
    @Transactional(readOnly = true)
    public List<VerticalResponseDto> getAllVerticalesWithOrgN2Fabrica() {
        List<Vertical> verticales = verticalRepository.findAllWithNucleus();
        
        return verticales.stream()
                .map(verticalMapper::mapToResponseDtoWithOrgN2Fabrica)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualiza una vertical con un nuevo nombre y asocia las fabricas con entidades Nucleus
     */
    public VerticalResponseDto updateVertical(Long verticalId, VerticalUpdateDto updateDto) {
        // Validaciones
        if (updateDto.getName() == null || updateDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        if (updateDto.getOrgN2fabricaList() == null) {
            throw new IllegalArgumentException("La lista de fabricas es obligatoria");
        }
        
        // Buscar la vertical existente
        Optional<Vertical> verticalOptional = verticalRepository.findById(verticalId);
        if (!verticalOptional.isPresent()) {
            throw new IllegalArgumentException("Vertical no encontrada con ID: " + verticalId);
        }
        
        Vertical vertical = verticalOptional.get();
        
        // Actualizar el nombre
        vertical.setName(updateDto.getName().trim());
        vertical.setRefVertical(updateDto.getRefVertical().toUpperCase().trim());

        // Eliminar todas las relaciones existentes de esta vertical
        verticalNucleusRepository.deleteByVerticalId(verticalId);

        // Buscar los Nucleus que coincidan con las fabricas proporcionadas
        updateFactories(updateDto.getOrgN2fabricaList(), vertical);

        // Guardar la vertical actualizada
        Vertical savedVertical = verticalRepository.save(vertical);
        
        // Retornar el DTO con la información actualizada
        return verticalMapper.mapToResponseDto(savedVertical);
    }

    private void updateFactories(List<String> factories, Vertical vertical) {
        List<Nucleus> nucleusList = nucleusRepository.findByOrgN2fabricaIn(factories);

        // Crear las nuevas relaciones VerticalNucleus
        List<VerticalNucleus> nuevasRelaciones = nucleusList.stream()
                .map(nucleus -> {
                    VerticalNucleus verticalNucleus = new VerticalNucleus();
                    verticalNucleus.setVertical(vertical);
                    verticalNucleus.setNucleus(nucleus);
                    return verticalNucleus;
                })
                .collect(Collectors.toList());

        // Guardar las nuevas relaciones
        verticalNucleusRepository.saveAll(nuevasRelaciones);
    }

    /**
     * Obtiene todas las OrgN1Fabricas (ServiceN1) con sus Verticales asociadas
     */
    @Transactional(readOnly = true)
    public List<OrgN2FabricaResponseDto> getAllOrgN2FabricasWithVerticales() {
        List<FactoryVertical> orgN2WithVerticales = nucleusRepository.findDistinctOrgN2WithVerticales();
        
        // Convertir directamente a DTOs ya que cada orgN1 solo puede tener una vertical
        return orgN2WithVerticales.stream()
                .map(result -> {
                    String orgN2 = result.getOrgN2();
                    Vertical vertical = result.getVertical();

                    OrgN2FabricaResponseDto dto = new OrgN2FabricaResponseDto();
                    dto.setOrgN2(orgN2);
                    VerticalResponseDto verticalDto = verticalMapper.mapToResponseDto(vertical);
                    if (verticalDto != null) {
                        dto.setRefVertical(vertical.getRefVertical());
                        dto.setVerticales(Collections.singletonList(verticalDto));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}