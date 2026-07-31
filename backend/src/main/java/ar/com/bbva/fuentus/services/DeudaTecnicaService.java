package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.DeudaTecnicaItemDTO;
import ar.com.bbva.fuentus.entities.App;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.SonarParam;
import ar.com.bbva.fuentus.repositories.AppsRepository;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.SonarParamsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class DeudaTecnicaService {

    @Autowired
    private SonarParamsRepository sonarParamRepository;

    @Autowired
    private AppsRepository appRepository;

    @Autowired
    private NucleusRepository nucleusRepository;

    public List<DeudaTecnicaItemDTO> getDeudaTecnicaPorNivel(String vertical, String fabrica, String sn1) {
        log.info("getDeudaTecnicaPorNivel called with: vertical={}, fabrica={}, sn1={}", vertical, fabrica, sn1);
        
        // Obtener todos los análisis de SonarQube
        List<SonarParam> allSonarParams = sonarParamRepository.findAll();
        log.info("Total SonarParams found: {}", allSonarParams.size());
        
        // Filtrar solo los análisis más recientes por app
        Map<Long, SonarParam> latestAnalysisByApp = getLatestAnalysisByApp(allSonarParams);
        log.info("Latest analysis by app: {}", latestAnalysisByApp.size());
        
        // Obtener todas las apps
        Map<Long, App> appsMap = appRepository.findAll().stream()
            .collect(Collectors.toMap(App::getId, app -> app));
        log.info("Total Apps found: {}", appsMap.size());
        
        // Obtener todos los servicios de Nucleus
        List<Nucleus> allNucleus = nucleusRepository.findAll();
        log.info("Total Nucleus found: {}", allNucleus.size());
        
        // Filtrar nucleus según filtros aplicados
        List<Nucleus> filteredNucleus = filterNucleus(allNucleus, vertical, fabrica, sn1);
        
        // Determinar qué nivel mostrar
        String nivelAMostrar = determinarNivelAMostrar(vertical, fabrica, sn1);
        
        // Agrupar y calcular métricas por nivel
        return calcularMetricasPorNivel(latestAnalysisByApp, appsMap, filteredNucleus, nivelAMostrar);
    }

    private Map<Long, SonarParam> getLatestAnalysisByApp(List<SonarParam> allSonarParams) {
        Map<Long, SonarParam> latestByApp = new HashMap<>();
        
        for (SonarParam param : allSonarParams) {
            if (param.getAppId() == 0 || param.getAnalisisDate() == null) continue;
            
            Long appId = param.getAppId();
            if (!latestByApp.containsKey(appId) || 
                param.getAnalisisDate().after(latestByApp.get(appId).getAnalisisDate())) {
                latestByApp.put(appId, param);
            }
        }
        
        return latestByApp;
    }

    private List<Nucleus> filterNucleus(List<Nucleus> allNucleus, String vertical, String fabrica, String sn1) {
        return allNucleus.stream()
            .filter(n -> vertical == null || vertical.equalsIgnoreCase(n.getArea()))
            .filter(n -> fabrica == null || fabrica.equalsIgnoreCase(n.getOrgN2fabrica()))
            .filter(n -> sn1 == null || sn1.equalsIgnoreCase(n.getServiceN1()))
            .collect(Collectors.toList());
    }

    private String determinarNivelAMostrar(String vertical, String fabrica, String sn1) {
        if (sn1 != null) return "SN2";
        if (fabrica != null) return "SN1";
        if (vertical != null) return "FABRICA";
        return "VERTICAL";
    }

    private List<DeudaTecnicaItemDTO> calcularMetricasPorNivel(
            Map<Long, SonarParam> latestAnalysisByApp,
            Map<Long, App> appsMap,
            List<Nucleus> filteredNucleus,
            String nivelAMostrar) {
        
        // Crear mapa de UUAA -> Nucleus para búsqueda rápida
        // Las UUAAs en nucleus pueden ser múltiples separadas por coma (ej: AWBS0000,AWCC0000)
        // y tienen formato de 4 letras + 4 ceros, mientras que en apps son solo 4 letras
        Map<String, Nucleus> nucleusByUuaa = new HashMap<>();
        for (Nucleus nucleus : filteredNucleus) {
            if (nucleus.getUuaa() == null || nucleus.getUuaa().trim().isEmpty()) {
                continue;
            }
            
            // Dividir por comas en caso de múltiples UUAAs
            String[] uuaas = nucleus.getUuaa().split(",");
            for (String uuaa : uuaas) {
                String cleanUuaa = uuaa.trim().toLowerCase();
                // Agregar versión completa (ej: awbs0000)
                nucleusByUuaa.putIfAbsent(cleanUuaa, nucleus);
                
                // Agregar versión sin los 4 ceros finales (ej: awbs)
                if (cleanUuaa.endsWith("0000") && cleanUuaa.length() > 4) {
                    String shortUuaa = cleanUuaa.substring(0, cleanUuaa.length() - 4);
                    nucleusByUuaa.putIfAbsent(shortUuaa, nucleus);
                }
            }
        }
        
        // Agrupar métricas por nivel
        Map<String, MetricasAcumuladas> metricasPorNivel = new HashMap<>();
        
        for (Map.Entry<Long, SonarParam> entry : latestAnalysisByApp.entrySet()) {
            Long appId = entry.getKey();
            SonarParam sonarParam = entry.getValue();
            
            // Obtener la app
            App app = appsMap.get(appId);
            if (app == null || app.getUuaa() == null) {
                continue;
            }
            
            // Obtener el nucleus asociado (buscar por UUAA corta, ej: "awbs")
            Nucleus nucleus = nucleusByUuaa.get(app.getUuaa().toLowerCase());
            if (nucleus == null) {
                continue;
            }
            
            // Obtener el label según el nivel
            String label = getLabelForNivel(nucleus, nivelAMostrar);
            if (label == null || label.isEmpty()) {
                continue;
            }
            
            // Acumular métricas
            metricasPorNivel.putIfAbsent(label, new MetricasAcumuladas());
            MetricasAcumuladas metricas = metricasPorNivel.get(label);
            
            metricas.totalBugs += (sonarParam.getBugs() != null ? sonarParam.getBugs().intValue() : 0);
            metricas.totalVulnerabilities += (sonarParam.getVulnerabilities() != null ? sonarParam.getVulnerabilities().intValue() : 0);
            // codeSmells es String, necesitamos parsearlo
            try {
                if (sonarParam.getCodeSmells() != null && !sonarParam.getCodeSmells().isEmpty()) {
                    metricas.totalCodeSmells += Integer.parseInt(sonarParam.getCodeSmells());
                }
            } catch (NumberFormatException e) {
                // Ignorar si no es un número válido
            }
            metricas.totalApps++;
        }
        
        // Convertir a DTOs y ordenar
        return metricasPorNivel.entrySet().stream()
            .map(entry -> new DeudaTecnicaItemDTO(
                entry.getKey(),
                nivelAMostrar,
                entry.getValue().totalBugs,
                entry.getValue().totalVulnerabilities,
                entry.getValue().totalCodeSmells,
                entry.getValue().totalApps
            ))
            .sorted(Comparator.comparing(DeudaTecnicaItemDTO::getLabel))
            .collect(Collectors.toList());
    }

    private String getLabelForNivel(Nucleus nucleus, String nivel) {
        switch (nivel) {
            case "VERTICAL":
                return nucleus.getArea();
            case "FABRICA":
                return nucleus.getOrgN2fabrica();
            case "SN1":
                return nucleus.getServiceN1();
            case "SN2":
                return nucleus.getServiceN2();
            default:
                return null;
        }
    }

    // Clase auxiliar para acumular métricas
    private static class MetricasAcumuladas {
        int totalBugs = 0;
        int totalVulnerabilities = 0;
        int totalCodeSmells = 0;
        int totalApps = 0;
    }
}
