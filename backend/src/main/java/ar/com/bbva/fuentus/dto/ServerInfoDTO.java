package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServerInfoDTO {

    private Long appServerId;

    private Long serverId;

    private String serverName;

    private String appName;



}
