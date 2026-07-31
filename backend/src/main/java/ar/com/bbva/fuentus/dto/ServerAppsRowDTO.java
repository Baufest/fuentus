package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerAppsRowDTO {

    private Long serverId;

    private String serverName;

    private Long appId;

    private String appName;
}
