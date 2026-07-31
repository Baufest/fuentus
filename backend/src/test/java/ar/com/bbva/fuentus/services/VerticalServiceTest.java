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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerticalServiceTest {

    @Mock
    private VerticalRepository verticalRepository;

    @Mock
    private VerticalMapper verticalMapper;

    @Mock
    private VerticalNucleusRepository verticalNucleusRepository;

    @Mock
    private NucleusRepository nucleusRepository;

    @InjectMocks
    private VerticalService verticalService;

    @Captor
    private ArgumentCaptor<Vertical> verticalCaptor;

    @Captor
    private ArgumentCaptor<List<VerticalNucleus>> relationsCaptor;

    private VerticalRequestDto buildRequestDto() {
        VerticalRequestDto requestDto = new VerticalRequestDto();
        requestDto.setName("  core vertical  ");
        requestDto.setRefVertical("ref01");
        requestDto.setOrgN2fabricaList(Arrays.asList("FactoryA", "FactoryB"));
        return requestDto;
    }

    private Nucleus buildNucleus(long id, String orgN2) {
        Nucleus nucleus = new Nucleus();
        nucleus.setId(id);
        nucleus.setOrgN2fabrica(orgN2);
        return nucleus;
    }

    @Test
    void createVertical_ShouldPersistVerticalAndFactories() {
        VerticalRequestDto requestDto = buildRequestDto();
        List<Nucleus> nucleusList = Arrays.asList(buildNucleus(1L, "FactoryA"), buildNucleus(2L, "FactoryB"));

        when(nucleusRepository.findByOrgN2fabricaIn(requestDto.getOrgN2fabricaList())).thenReturn(nucleusList);
        when(verticalRepository.save(any(Vertical.class))).thenAnswer(invocation -> {
            Vertical vertical = invocation.getArgument(0);
            vertical.setId(10L);
            return vertical;
        });

        VerticalResponseDto expectedResponse = new VerticalResponseDto();
        expectedResponse.setId(10L);
        expectedResponse.setName("CORE VERTICAL");
        when(verticalMapper.mapToResponseDto(any(Vertical.class))).thenReturn(expectedResponse);

        VerticalResponseDto result = verticalService.createVertical(requestDto);

        assertSame(expectedResponse, result);

        verify(verticalRepository, times(1)).save(verticalCaptor.capture());
        Vertical savedVertical = verticalCaptor.getValue();
        assertEquals("CORE VERTICAL", savedVertical.getName());
        assertEquals("REF01", savedVertical.getRefVertical());

        verify(nucleusRepository, times(1)).findByOrgN2fabricaIn(requestDto.getOrgN2fabricaList());
        verify(verticalNucleusRepository, times(1)).saveAll(relationsCaptor.capture());

        List<VerticalNucleus> savedRelations = relationsCaptor.getValue();
        assertEquals(2, savedRelations.size());
        assertTrue(savedRelations.stream().allMatch(relation -> relation.getVertical() == savedVertical));

        List<Nucleus> relatedNucleus = savedRelations.stream()
                .map(VerticalNucleus::getNucleus)
                .collect(Collectors.toList());
        assertTrue(relatedNucleus.containsAll(nucleusList));
    }

    @Test
    void createVertical_ShouldThrowWhenNameMissing() {
        VerticalRequestDto requestDto = new VerticalRequestDto();
        requestDto.setName("   ");
        requestDto.setRefVertical("any");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> verticalService.createVertical(requestDto));

        assertEquals("El nombre es obligatorio", exception.getMessage());
        verifyNoInteractions(verticalRepository, verticalMapper, verticalNucleusRepository, nucleusRepository);
    }

    @Test
    void updateVertical_ShouldUpdateExistingVerticalAndFactories() {
        Long verticalId = 5L;
        VerticalUpdateDto updateDto = new VerticalUpdateDto("  nuevo nombre  ", "refNuevo", Collections.singletonList("FactoryX"));
        Vertical existingVertical = new Vertical();
        existingVertical.setId(verticalId);
        existingVertical.setName("old");
        existingVertical.setRefVertical("oldRef");

        when(verticalRepository.findById(verticalId)).thenReturn(Optional.of(existingVertical));
        when(nucleusRepository.findByOrgN2fabricaIn(updateDto.getOrgN2fabricaList()))
                .thenReturn(Collections.singletonList(buildNucleus(9L, "FactoryX")));
        when(verticalRepository.save(existingVertical)).thenReturn(existingVertical);

        VerticalResponseDto mappedResponse = new VerticalResponseDto();
        when(verticalMapper.mapToResponseDto(existingVertical)).thenReturn(mappedResponse);

        VerticalResponseDto result = verticalService.updateVertical(verticalId, updateDto);

        assertSame(mappedResponse, result);
        assertEquals("nuevo nombre", existingVertical.getName());
        assertEquals("REFNUEVO", existingVertical.getRefVertical());

        verify(verticalNucleusRepository, times(1)).deleteByVerticalId(verticalId);
        verify(verticalRepository, times(1)).save(existingVertical);
        verify(verticalNucleusRepository, times(1)).saveAll(relationsCaptor.capture());

        List<VerticalNucleus> relations = relationsCaptor.getValue();
        assertEquals(1, relations.size());
        assertEquals(existingVertical, relations.get(0).getVertical());
        assertEquals("FactoryX", relations.get(0).getNucleus().getOrgN2fabrica());
    }

    @Test
    void updateVertical_ShouldThrowWhenVerticalNotFound() {
        Long verticalId = 8L;
        VerticalUpdateDto updateDto = new VerticalUpdateDto("Name", "Ref", Collections.singletonList("Factory"));
        when(verticalRepository.findById(verticalId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> verticalService.updateVertical(verticalId, updateDto));

        assertTrue(exception.getMessage().contains("Vertical no encontrada"));
        verify(verticalRepository, times(1)).findById(verticalId);
        verifyNoMoreInteractions(verticalRepository);
        verifyNoInteractions(verticalMapper, verticalNucleusRepository, nucleusRepository);
    }

    @Test
    void updateVertical_ShouldThrowWhenNameInvalid() {
        VerticalUpdateDto updateDto = new VerticalUpdateDto("  ", "ref", Collections.singletonList("Factory"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> verticalService.updateVertical(1L, updateDto));

        assertEquals("El nombre es obligatorio", exception.getMessage());
        verifyNoInteractions(verticalRepository, verticalMapper, verticalNucleusRepository, nucleusRepository);
    }

    @Test
    void updateVertical_ShouldThrowWhenFactoriesNull() {
        VerticalUpdateDto updateDto = new VerticalUpdateDto("Name", "ref", null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> verticalService.updateVertical(2L, updateDto));

        assertEquals("La lista de fabricas es obligatoria", exception.getMessage());
        verifyNoInteractions(verticalRepository, verticalMapper, verticalNucleusRepository, nucleusRepository);
    }

    @Test
    void getAllVerticalesWithOrgN2Fabrica_ShouldReturnMappedDtos() {
        Vertical verticalOne = new Vertical();
        Vertical verticalTwo = new Vertical();
        VerticalResponseDto dtoOne = new VerticalResponseDto();
        VerticalResponseDto dtoTwo = new VerticalResponseDto();

        when(verticalRepository.findAllWithNucleus()).thenReturn(Arrays.asList(verticalOne, verticalTwo));
        when(verticalMapper.mapToResponseDtoWithOrgN2Fabrica(verticalOne)).thenReturn(dtoOne);
        when(verticalMapper.mapToResponseDtoWithOrgN2Fabrica(verticalTwo)).thenReturn(dtoTwo);

        List<VerticalResponseDto> result = verticalService.getAllVerticalesWithOrgN2Fabrica();

        assertEquals(Arrays.asList(dtoOne, dtoTwo), result);
        verify(verticalRepository, times(1)).findAllWithNucleus();
        verify(verticalMapper, times(1)).mapToResponseDtoWithOrgN2Fabrica(verticalOne);
        verify(verticalMapper, times(1)).mapToResponseDtoWithOrgN2Fabrica(verticalTwo);
    }

    @Test
    void getAllOrgN2FabricasWithVerticales_ShouldReturnOrgN2Dtos() {
        Vertical vertical = new Vertical();
        vertical.setRefVertical("REF-V");
        FactoryVertical factoryVertical = new FactoryVertical("ORG-N2", vertical);
        VerticalResponseDto verticalResponseDto = new VerticalResponseDto();

        when(nucleusRepository.findDistinctOrgN2WithVerticales()).thenReturn(Collections.singletonList(factoryVertical));
        when(verticalMapper.mapToResponseDto(vertical)).thenReturn(verticalResponseDto);

        List<OrgN2FabricaResponseDto> result = verticalService.getAllOrgN2FabricasWithVerticales();

        assertEquals(1, result.size());
        OrgN2FabricaResponseDto dto = result.get(0);
        assertEquals("ORG-N2", dto.getOrgN2());
        assertEquals("REF-V", dto.getRefVertical());
        assertEquals(Collections.singletonList(verticalResponseDto), dto.getVerticales());
    }

    @Test
    void getAllOrgN2FabricasWithVerticales_ShouldHandleNullMapperResult() {
        Vertical vertical = new Vertical();
        vertical.setRefVertical("ref");
        FactoryVertical factoryVertical = new FactoryVertical("ORG", vertical);

        when(nucleusRepository.findDistinctOrgN2WithVerticales()).thenReturn(Collections.singletonList(factoryVertical));
        when(verticalMapper.mapToResponseDto(vertical)).thenReturn(null);

        List<OrgN2FabricaResponseDto> result = verticalService.getAllOrgN2FabricasWithVerticales();

        assertEquals(1, result.size());
        OrgN2FabricaResponseDto dto = result.get(0);
        assertEquals("ORG", dto.getOrgN2());
        assertNull(dto.getRefVertical());
        assertNull(dto.getVerticales());
    }
}
