package ar.com.bbva.fuentus.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceSummaryDTOTest {

    @Test
    void constructor_ShouldCreateEmptyDTO() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        
        assertNull(dto.getServiceId());
        assertNull(dto.getServiceN1());
        assertNull(dto.getServiceN2());
        assertNull(dto.getOwnerServiceN1());
        assertNull(dto.getUuaas());
        assertNull(dto.getTotalApps());
        assertNull(dto.getAverageCoverage());
        assertNull(dto.getTotalBugs());
    }

    @Test
    void allArgsConstructor_ShouldPopulateAllFields() {
        List<String> uuaas = Arrays.asList("FUET", "TEST");
        
        ServiceSummaryDTO dto = new ServiceSummaryDTO(
            1L,                    // serviceId
            "Service N1",          // serviceN1
            "Service N2",          // serviceN2
            "Owner N1",            // ownerServiceN1
            uuaas,                 // uuaas
            10,                    // totalApps
            75.5,                  // averageCoverage
            5L,                    // totalBugs
            2L,                    // totalSastLow
            3L,                    // totalSastMedium
            1L,                    // totalSastHigh
            4L,                    // totalScaLow
            5L,                    // totalScaMedium
            2L,                    // totalScaHigh
            1L,                    // totalScaCritical
            100L,                  // rfoId
            "ACTIVO"               // rfoEstado
        );

        assertAll(
            () -> assertEquals(1L, dto.getServiceId()),
            () -> assertEquals("Service N1", dto.getServiceN1()),
            () -> assertEquals("Service N2", dto.getServiceN2()),
            () -> assertEquals("Owner N1", dto.getOwnerServiceN1()),
            () -> assertEquals(uuaas, dto.getUuaas()),
            () -> assertEquals(10, dto.getTotalApps()),
            () -> assertEquals(75.5, dto.getAverageCoverage()),
            () -> assertEquals(5L, dto.getTotalBugs()),
            () -> assertEquals(2L, dto.getTotalSastLow()),
            () -> assertEquals(3L, dto.getTotalSastMedium()),
            () -> assertEquals(1L, dto.getTotalSastHigh()),
            () -> assertEquals(4L, dto.getTotalScaLow()),
            () -> assertEquals(5L, dto.getTotalScaMedium()),
            () -> assertEquals(2L, dto.getTotalScaHigh()),
            () -> assertEquals(1L, dto.getTotalScaCritical()),
            () -> assertEquals(100L, dto.getRfoId()),
            () -> assertEquals("ACTIVO", dto.getRfoEstado())
        );
    }

    @Test
    void setters_ShouldUpdateFields() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        List<String> uuaas = Arrays.asList("FUET", "TEST");
        
        dto.setServiceId(1L);
        dto.setServiceN1("Service N1");
        dto.setServiceN2("Service N2");
        dto.setOwnerServiceN1("Owner N1");
        dto.setUuaas(uuaas);
        dto.setTotalApps(10);
        dto.setAverageCoverage(75.5);
        dto.setTotalBugs(5L);
        dto.setTotalSastLow(2L);
        dto.setTotalSastMedium(3L);
        dto.setTotalSastHigh(1L);
        dto.setTotalScaLow(4L);
        dto.setTotalScaMedium(5L);
        dto.setTotalScaHigh(2L);
        dto.setTotalScaCritical(1L);
        dto.setRfoId(100L);
        dto.setRfoEstado("ACTIVO");

        assertAll(
            () -> assertEquals(1L, dto.getServiceId()),
            () -> assertEquals("Service N1", dto.getServiceN1()),
            () -> assertEquals("Service N2", dto.getServiceN2()),
            () -> assertEquals("Owner N1", dto.getOwnerServiceN1()),
            () -> assertEquals(uuaas, dto.getUuaas()),
            () -> assertEquals(10, dto.getTotalApps()),
            () -> assertEquals(75.5, dto.getAverageCoverage()),
            () -> assertEquals(5L, dto.getTotalBugs()),
            () -> assertEquals(2L, dto.getTotalSastLow()),
            () -> assertEquals(3L, dto.getTotalSastMedium()),
            () -> assertEquals(1L, dto.getTotalSastHigh()),
            () -> assertEquals(4L, dto.getTotalScaLow()),
            () -> assertEquals(5L, dto.getTotalScaMedium()),
            () -> assertEquals(2L, dto.getTotalScaHigh()),
            () -> assertEquals(1L, dto.getTotalScaCritical()),
            () -> assertEquals(100L, dto.getRfoId()),
            () -> assertEquals("ACTIVO", dto.getRfoEstado())
        );
    }

    @Test
    void getTotalSastVulnerabilities_ShouldSumAllSastLevels() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        dto.setTotalSastLow(2L);
        dto.setTotalSastMedium(3L);
        dto.setTotalSastHigh(1L);

        assertEquals(6L, dto.getTotalSastVulnerabilities());
    }

    @Test
    void getTotalSastVulnerabilities_ShouldHandleNullValues() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        dto.setTotalSastLow(null);
        dto.setTotalSastMedium(3L);
        dto.setTotalSastHigh(null);

        assertEquals(3L, dto.getTotalSastVulnerabilities());
    }

    @Test
    void getTotalSastVulnerabilities_ShouldReturnZeroWhenAllNull() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        
        assertEquals(0L, dto.getTotalSastVulnerabilities());
    }

    @Test
    void getTotalScaVulnerabilities_ShouldSumAllScaLevels() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        dto.setTotalScaLow(4L);
        dto.setTotalScaMedium(5L);
        dto.setTotalScaHigh(2L);
        dto.setTotalScaCritical(1L);

        assertEquals(12L, dto.getTotalScaVulnerabilities());
    }

    @Test
    void getTotalScaVulnerabilities_ShouldHandleNullValues() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        dto.setTotalScaLow(null);
        dto.setTotalScaMedium(5L);
        dto.setTotalScaHigh(null);
        dto.setTotalScaCritical(1L);

        assertEquals(6L, dto.getTotalScaVulnerabilities());
    }

    @Test
    void getTotalScaVulnerabilities_ShouldReturnZeroWhenAllNull() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        
        assertEquals(0L, dto.getTotalScaVulnerabilities());
    }

    @Test
    void getTotalVulnerabilities_ShouldSumSastAndSca() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        dto.setTotalSastLow(2L);
        dto.setTotalSastMedium(3L);
        dto.setTotalSastHigh(1L);
        dto.setTotalScaLow(4L);
        dto.setTotalScaMedium(5L);
        dto.setTotalScaHigh(2L);
        dto.setTotalScaCritical(1L);

        assertEquals(18L, dto.getTotalVulnerabilities());
    }

    @Test
    void getTotalVulnerabilities_ShouldHandleNullValues() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        dto.setTotalSastLow(2L);
        dto.setTotalSastMedium(null);
        dto.setTotalSastHigh(1L);
        dto.setTotalScaLow(null);
        dto.setTotalScaMedium(5L);
        dto.setTotalScaHigh(null);
        dto.setTotalScaCritical(1L);

        assertEquals(9L, dto.getTotalVulnerabilities());
    }

    @Test
    void getTotalVulnerabilities_ShouldReturnZeroWhenAllNull() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        
        assertEquals(0L, dto.getTotalVulnerabilities());
    }

    @Test
    void complexScenario_ShouldCalculateCorrectTotals() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        dto.setServiceId(1L);
        dto.setServiceN1("Core Services");
        dto.setServiceN2("Authentication");
        dto.setTotalApps(15);
        
        // Set mixed null and non-null values
        dto.setTotalSastLow(10L);
        dto.setTotalSastMedium(null);
        dto.setTotalSastHigh(5L);
        dto.setTotalScaLow(3L);
        dto.setTotalScaMedium(7L);
        dto.setTotalScaHigh(null);
        dto.setTotalScaCritical(2L);

        assertAll(
            () -> assertEquals(15L, dto.getTotalSastVulnerabilities(), "SAST total should be 15"),
            () -> assertEquals(12L, dto.getTotalScaVulnerabilities(), "SCA total should be 12"),
            () -> assertEquals(27L, dto.getTotalVulnerabilities(), "Total vulnerabilities should be 27")
        );
    }

    @Test
    void uuaasList_ShouldHandleMultipleValues() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        List<String> uuaas = Arrays.asList("FUET", "TEST", "DEMO", "PROD");
        
        dto.setUuaas(uuaas);

        assertEquals(4, dto.getUuaas().size());
        assertTrue(dto.getUuaas().contains("FUET"));
        assertTrue(dto.getUuaas().contains("TEST"));
        assertTrue(dto.getUuaas().contains("DEMO"));
        assertTrue(dto.getUuaas().contains("PROD"));
    }

    @Test
    void averageCoverage_ShouldAcceptDecimalValues() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        
        dto.setAverageCoverage(87.65);
        
        assertEquals(87.65, dto.getAverageCoverage(), 0.001);
    }

    @Test
    void rfoFields_ShouldStoreCorrectly() {
        ServiceSummaryDTO dto = new ServiceSummaryDTO();
        
        dto.setRfoId(999L);
        dto.setRfoEstado("INACTIVO");
        
        assertAll(
            () -> assertEquals(999L, dto.getRfoId()),
            () -> assertEquals("INACTIVO", dto.getRfoEstado())
        );
    }
}
