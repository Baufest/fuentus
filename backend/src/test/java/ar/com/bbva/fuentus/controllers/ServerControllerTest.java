package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.ServerNameDTO;
import ar.com.bbva.fuentus.dto.ServerWithAppsDTO;
import ar.com.bbva.fuentus.services.ServerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServerController.class)
class ServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServerService serverService;

    private ServerWithAppsDTO serverResponse;
    private List<ServerNameDTO> serverNames;

    @BeforeEach
    void setUp() {
        ServerWithAppsDTO.AppInfo firstApp = new ServerWithAppsDTO.AppInfo(100L, "app-one");
        serverResponse = new ServerWithAppsDTO(1L, "srv-one", Arrays.asList(firstApp));
        serverNames = Arrays.asList(new ServerNameDTO(1L, "srv-one"), new ServerNameDTO(2L, "srv-two"));
    }

    @Test
    void getServers_ShouldReturnServersWithApps_WhenNoFiltersProvided() throws Exception {
        when(serverService.getServersWithApps(null, null)).thenReturn(Collections.singletonList(serverResponse));

        mockMvc.perform(get("/servers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("srv-one"))
                .andExpect(jsonPath("$[0].apps[0].id").value(100))
                .andExpect(jsonPath("$[0].apps[0].name").value("app-one"));
    }

    @Test
    void getServers_ShouldForwardFiltersToService_WhenFiltersProvided() throws Exception {
        String serverName = "srv-two";
        String appName = "test";
        when(serverService.getServersWithApps(serverName, appName)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/servers")
                        .param("name", serverName)
                        .param("appName", appName))
                .andExpect(status().isOk());

        verify(serverService).getServersWithApps(serverName, appName);
    }

    @Test
    void getServerNames_ShouldReturnAllNames() throws Exception {
        when(serverService.getServerNames()).thenReturn(serverNames);

        mockMvc.perform(get("/servers/names"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("srv-one"));

        verify(serverService).getServerNames();
    }
}
