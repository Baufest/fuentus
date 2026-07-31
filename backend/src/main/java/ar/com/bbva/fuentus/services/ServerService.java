package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.ServerAppsRowDTO;
import ar.com.bbva.fuentus.dto.ServerNameDTO;
import ar.com.bbva.fuentus.dto.ServerWithAppsDTO;
import ar.com.bbva.fuentus.repositories.ServerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServerService {

    private final ServerRepository serverRepository;

    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public List<ServerWithAppsDTO> getServersWithApps(String serverName, String appNameFilter) {
        String normalizedServerName = normalize(serverName);
        String normalizedAppNameFilter = normalize(appNameFilter);

        List<ServerAppsRowDTO> rows = serverRepository.findServersWithApps(normalizedServerName, normalizedAppNameFilter);

        Map<Long, ServerWithAppsDTO> groupedByServer = new LinkedHashMap<>();
        for (ServerAppsRowDTO row : rows) {
            ServerWithAppsDTO server = groupedByServer.computeIfAbsent(
                    row.getServerId(),
                    id -> new ServerWithAppsDTO(id, row.getServerName(), new ArrayList<>())
            );
            server.getApps().add(new ServerWithAppsDTO.AppInfo(row.getAppId(), row.getAppName()));
        }

        return new ArrayList<>(groupedByServer.values());
    }

    public List<ServerNameDTO> getServerNames() {
        return serverRepository.findAllServerNames();
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
