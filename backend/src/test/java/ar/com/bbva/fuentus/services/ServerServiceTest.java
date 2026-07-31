package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.ServerAppsRowDTO;
import ar.com.bbva.fuentus.dto.ServerNameDTO;
import ar.com.bbva.fuentus.dto.ServerWithAppsDTO;
import ar.com.bbva.fuentus.repositories.ServerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServerServiceTest {

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private ServerService serverService;

    @Test
    void getServersWithApps_shouldGroupAppsByServer() {
        List<ServerAppsRowDTO> rows = Arrays.asList(
            new ServerAppsRowDTO(1L, "Server-A", 101L, "App-Alpha"),
            new ServerAppsRowDTO(1L, "Server-A", 102L, "App-Beta"),
            new ServerAppsRowDTO(2L, "Server-B", 201L, "App-Gamma")
        );
        when(serverRepository.findServersWithApps(null, null)).thenReturn(rows);

        List<ServerWithAppsDTO> result = serverService.getServersWithApps(null, null);

        assertEquals(2, result.size());

        ServerWithAppsDTO firstServer = result.get(0);
        assertEquals(Long.valueOf(1L), firstServer.getId());
        assertEquals("Server-A", firstServer.getName());
        assertEquals(2, firstServer.getApps().size());
        assertEquals("App-Alpha", firstServer.getApps().get(0).getName());
        assertEquals("App-Beta", firstServer.getApps().get(1).getName());

        ServerWithAppsDTO secondServer = result.get(1);
        assertEquals(Long.valueOf(2L), secondServer.getId());
        assertEquals("Server-B", secondServer.getName());
        assertEquals(1, secondServer.getApps().size());
        assertEquals("App-Gamma", secondServer.getApps().get(0).getName());

        verify(serverRepository).findServersWithApps(null, null);
    }

    @Test
    void getServersWithApps_shouldNormalizeFiltersBeforeQuery() {
        when(serverRepository.findServersWithApps("Server-A", "App-X"))
            .thenReturn(Collections.emptyList());

        serverService.getServersWithApps("  Server-A  ", "  App-X \t");

        verify(serverRepository).findServersWithApps("Server-A", "App-X");
    }

    @Test
    void getServersWithApps_shouldConvertBlankFiltersToNull() {
        when(serverRepository.findServersWithApps(null, null)).thenReturn(Collections.emptyList());

        serverService.getServersWithApps("   ", "\t\n");

        verify(serverRepository).findServersWithApps(null, null);
    }

    @Test
    void getServerNames_shouldReturnRepositoryResult() {
        List<ServerNameDTO> serverNames = Arrays.asList(
            new ServerNameDTO(1L, "Server-A"),
            new ServerNameDTO(2L, "Server-B")
        );
        when(serverRepository.findAllServerNames()).thenReturn(serverNames);

        List<ServerNameDTO> result = serverService.getServerNames();

        assertSame(serverNames, result);
        verify(serverRepository).findAllServerNames();
    }
}
