package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerWithAppsDTO {

    private Long id;

    private String name;

    private List<AppInfo> apps = new ArrayList<>();

    public ServerWithAppsDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addApp(AppInfo appInfo) {
        if (apps == null) {
            apps = new ArrayList<>();
        }
        apps.add(appInfo);
    }

    @Getter
    @AllArgsConstructor
    public static class AppInfo {
        private Long id;
        private String name;
    }
}
