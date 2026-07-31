package ar.com.bbva.fuentus.processors;

import ar.com.bbva.fuentus.enums.EntityType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProcessorFactoryTest {

    @Mock
    private NucleusProcessor nucleusProcessor;

    @Mock
    private ChimeraScaProcessor chimeraScaProcessor;

    @Mock
    private ChimeraSastProcessor chimeraSastProcessor;

    @Mock
    private AppProcessor appProcessor;

    @Mock
    private RfoProcessor rfoProcessor;

    @Mock
    private ProductividadProcessor productividadProcessor;

    @Mock
    private VelocidadProcessor velocidadProcessor;

    @InjectMocks
    private ProcessorFactory processorFactory;

    @Test
    void getProcessor_ShouldReturnNucleusProcessor_WhenEntityTypeIsNucleusServices() {
        // When
        EntityProcessor result = processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES);

        // Then
        assertNotNull(result);
        assertSame(nucleusProcessor, result);
    }

    @Test
    void getProcessor_ShouldReturnChimeraScaProcessor_WhenEntityTypeIsChimeraSca() {
        // When
        EntityProcessor result = processorFactory.getProcessor(EntityType.CHIMERA_SCA);

        // Then
        assertNotNull(result);
        assertSame(chimeraScaProcessor, result);
    }

    @Test
    void getProcessor_ShouldReturnChimeraSastProcessor_WhenEntityTypeIsChimeraSast() {
        // When
        EntityProcessor result = processorFactory.getProcessor(EntityType.CHIMERA_SAST);

        // Then
        assertNotNull(result);
        assertSame(chimeraSastProcessor, result);
    }

    @Test
    void getProcessor_ShouldReturnAppProcessor_WhenEntityTypeIsApps() {
        // When
        EntityProcessor result = processorFactory.getProcessor(EntityType.APPS);

        // Then
        assertNotNull(result);
        assertSame(appProcessor, result);
    }

    @Test
    void getProcessor_ShouldReturnRfoProcessor_WhenEntityTypeIsRfo() {
        // When
        EntityProcessor result = processorFactory.getProcessor(EntityType.RFO);

        // Then
        assertNotNull(result);
        assertSame(rfoProcessor, result);
    }

    @Test
    void getProcessor_ShouldReturnProductividadProcessor_WhenEntityTypeIsProductividad() {
        // When
        EntityProcessor result = processorFactory.getProcessor(EntityType.PRODUCTIVIDAD);

        // Then
        assertNotNull(result);
        assertSame(productividadProcessor, result);
    }

    @Test
    void getProcessor_ShouldReturnVelocidadProcessor_WhenEntityTypeIsVelocidad() {
        // When
        EntityProcessor result = processorFactory.getProcessor(EntityType.VELOCIDAD);

        // Then
        assertNotNull(result);
        assertSame(velocidadProcessor, result);
    }

    @Test
    void getProcessor_ShouldThrowException_WhenEntityTypeIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> processorFactory.getProcessor(null));
    }

    @Test
    void getProcessor_ShouldReturnSameInstanceOnMultipleCalls() {
        // When
        EntityProcessor result1 = processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES);
        EntityProcessor result2 = processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES);

        // Then
        assertSame(result1, result2);
    }

    @Test
    void getProcessor_ShouldReturnDifferentProcessorsForDifferentTypes() {
        // When
        EntityProcessor nucleusProc = processorFactory.getProcessor(EntityType.NUCLEUS_SERVICES);
        EntityProcessor appProc = processorFactory.getProcessor(EntityType.APPS);
        EntityProcessor rfoProc = processorFactory.getProcessor(EntityType.RFO);

        // Then
        assertNotSame(nucleusProc, appProc);
        assertNotSame(nucleusProc, rfoProc);
        assertNotSame(appProc, rfoProc);
    }

    @Test
    void getProcessor_ShouldHandleAllEntityTypes() {
        // Given - Array de todos los tipos de entidad
        EntityType[] allTypes = EntityType.values();

        // When & Then - Verificar que cada tipo retorna un processor no nulo
        for (EntityType type : allTypes) {
            EntityProcessor processor = processorFactory.getProcessor(type);
            assertNotNull(processor, "Processor should not be null for type: " + type);
        }
    }
}
