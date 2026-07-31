package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.ServerNameDTO;
import ar.com.bbva.fuentus.dto.ServerWithAppsDTO;
import ar.com.bbva.fuentus.services.ServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/servers")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping
    public ResponseEntity<List<ServerWithAppsDTO>> getServers(
            @RequestParam(name = "name", required = false) String serverName,
            @RequestParam(name = "appName", required = false) String appNameFilter) {

        List<ServerWithAppsDTO> servers = serverService.getServersWithApps(serverName, appNameFilter);
        return ResponseEntity.ok(servers);
    }

    @GetMapping("/names")
    public ResponseEntity<List<ServerNameDTO>> getServerNames() {
        return ResponseEntity.ok(serverService.getServerNames());
    }
}
