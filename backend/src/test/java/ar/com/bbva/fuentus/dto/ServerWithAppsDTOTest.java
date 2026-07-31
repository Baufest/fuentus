package ar.com.bbva.fuentus.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerWithAppsDTOTest {

    @Test
    void noArgsConstructor_ShouldStartWithEmptyAppList() {
        ServerWithAppsDTO dto = new ServerWithAppsDTO();

        assertAll(
            () -> assertNull(dto.getId()),
            () -> assertNull(dto.getName()),
            () -> assertNotNull(dto.getApps()),
            () -> assertTrue(dto.getApps().isEmpty())
        );
    }

    @Test
    void allArgsConstructor_ShouldPopulateFields() {
        List<ServerWithAppsDTO.AppInfo> apps = Arrays.asList(
            new ServerWithAppsDTO.AppInfo(1L, "Gateway"),
            new ServerWithAppsDTO.AppInfo(2L, "Scheduler")
        );

        ServerWithAppsDTO dto = new ServerWithAppsDTO(99L, "server-99", apps);

        assertAll(
            () -> assertEquals(99L, dto.getId()),
            () -> assertEquals("server-99", dto.getName()),
            () -> assertEquals(apps, dto.getApps())
        );
    }

    @Test
    void addApp_ShouldAppendToExistingList() {
        ServerWithAppsDTO dto = new ServerWithAppsDTO(5L, "server-5");

        dto.addApp(new ServerWithAppsDTO.AppInfo(1L, "Gateway"));
        dto.addApp(new ServerWithAppsDTO.AppInfo(2L, "Batch"));

        assertAll(
            () -> assertEquals(2, dto.getApps().size()),
            () -> assertEquals(2L, dto.getApps().get(1).getId()),
            () -> assertEquals("Batch", dto.getApps().get(1).getName())
        );
    }

    @Test
    void addApp_ShouldRecreateListWhenNull() {
        ServerWithAppsDTO dto = new ServerWithAppsDTO();
        dto.setApps(null);

        dto.addApp(new ServerWithAppsDTO.AppInfo(3L, "Console"));

        assertAll(
            () -> assertNotNull(dto.getApps()),
            () -> assertEquals(1, dto.getApps().size()),
            () -> assertEquals(3L, dto.getApps().get(0).getId()),
            () -> assertEquals("Console", dto.getApps().get(0).getName())
        );
    }

    @Test
    void appInfo_ShouldExposeIdAndName() {
        ServerWithAppsDTO.AppInfo appInfo = new ServerWithAppsDTO.AppInfo(7L, "Collector");

        assertAll(
            () -> assertEquals(7L, appInfo.getId()),
            () -> assertEquals("Collector", appInfo.getName())
        );
    }
}
