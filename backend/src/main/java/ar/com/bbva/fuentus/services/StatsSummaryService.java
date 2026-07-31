package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.AppSummaryDTO;
import ar.com.bbva.fuentus.dto.CountSummaryDTO;
import ar.com.bbva.fuentus.dto.NucleusCoverageStatsSummary;
import ar.com.bbva.fuentus.dto.NucleusGetRequestDTO;
import ar.com.bbva.fuentus.dto.NucleusLevel;
import ar.com.bbva.fuentus.dto.StatsSummaryDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.VerticalNucleus;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.SonarParamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class StatsSummaryService {

    @Autowired
    private NucleusService nucleusService;
    
    @Autowired
    private AppService appService;
    
    @Autowired
    private NucleusRepository nucleusRepository;
    
    @Autowired
    private SonarParamsRepository sonarParamsRepository;

    public List<StatsSummaryDTO> getStatsSummaryByFilters(String area, String orgN1, String orgN2fabrica, int page) {
        System.out.println("=== StatsSummaryService: Iniciando búsqueda ===");
        System.out.println("Parámetros: area=" + area + ", orgN1=" + orgN1 + ", orgN2fabrica=" + orgN2fabrica + ", page=" + page);
        
        // 1. Obtener TODOS los nucleus filtrados (todas las páginas)
        List<NucleusGetRequestDTO> allNucleus = new ArrayList<>();
        int currentPage = 0;
        boolean hasMorePages = true;
        
        while (hasMorePages) {
            Page<NucleusGetRequestDTO> nucleusPage = nucleusService.getNucleusByOptionalFields(area, orgN1, orgN2fabrica, currentPage);
            allNucleus.addAll(nucleusPage.getContent());
            hasMorePages = nucleusPage.hasNext();
            currentPage++;
            System.out.println("Página " + currentPage + ": " + nucleusPage.getContent().size() + " nucleus encontrados");
        }
        
        System.out.println("Total nucleus encontrados: " + allNucleus.size());
        
        // 2. Extraer todas las UUAAs únicas de todos los nucleus
        Map<String, StatsSummaryDTO> uuaaStatsMap = new HashMap<>();
        
        for (NucleusGetRequestDTO nucleus : allNucleus) {
            if (nucleus.getUuaa() != null) {
                for (String uuaa : nucleus.getUuaa()) {
                    if (!uuaaStatsMap.containsKey(uuaa)) {
                        uuaaStatsMap.put(uuaa, new StatsSummaryDTO(uuaa));
                        System.out.println("UUAA encontrada: " + uuaa);
                    }
                }
            }
        }
        
        System.out.println("Total UUAAs únicas: " + uuaaStatsMap.size());
        
        // 3. Para cada UUAA, obtener TODAS sus aplicaciones usando el método sin paginación
        for (String uuaa : uuaaStatsMap.keySet()) {
            List<AppSummaryDTO> allApps = appService.getAllAppsByUUAAComplete(uuaa);
            StatsSummaryDTO statsDTO = uuaaStatsMap.get(uuaa);
            
            System.out.println("UUAA: " + uuaa + " - Aplicaciones encontradas: " + allApps.size());

            for (AppSummaryDTO app : allApps) {
                System.out.println("  - App: " + app.getName() + " - BitBucket: " + app.getBitbucketUrl());
                
                StatsSummaryDTO.RepositoryStatsDTO repoStats = new StatsSummaryDTO.RepositoryStatsDTO(
                    app.getName(),
                    app.getBitbucketUrl(),
                    app.getLanguage(),
                    app.getMonolith()
                );
                
                // Agregar información de servidores
                if (app.getServers() != null) {
                    repoStats.getServers().addAll(app.getServers());
                }
                
                // Crear información de Sonar
                StatsSummaryDTO.SonarInfoDTO sonarInfo = new StatsSummaryDTO.SonarInfoDTO(
                    app.getSonarUrl(),
                    app.getSonar10Url(),
                    app.getCoverage(),
                    app.getBugs(),
                    app.getChimeraSast() != null ? app.getChimeraSast().getTotalLow() : null,
                    app.getChimeraSast() != null ? app.getChimeraSast().getTotalMedium() : null,
                    app.getChimeraSast() != null ? app.getChimeraSast().getTotalHigh() : null,
                    app.getChimeraSca() != null ? app.getChimeraSca().getTotalLow() : null,
                    app.getChimeraSca() != null ? app.getChimeraSca().getTotalMedium() : null,
                    app.getChimeraSca() != null ? app.getChimeraSca().getTotalHigh() : null,
                    app.getChimeraSca() != null ? app.getChimeraSca().getTotalCritical() : null
                );
                
                repoStats.setSonarInfo(sonarInfo);
                statsDTO.addRepository(repoStats);
            }
        }
        
        System.out.println("=== Resultado final ===");
        System.out.println("Total UUAAs en resultado: " + uuaaStatsMap.size());
        for (StatsSummaryDTO stats : uuaaStatsMap.values()) {
            System.out.println("UUAA: " + stats.getUuaa() + " - Repositorios: " + stats.getRepositories().size());
        }
        
        return new ArrayList<>(uuaaStatsMap.values());
    }
    
    public CountSummaryDTO getCountSummary(String vertical, String fabrica, String sn1, String sn2, String uuaa) {
        System.out.println("=== StatsSummaryService: Iniciando conteo ===");
        System.out.println("Parámetros: vertical=" + vertical + ", fabrica=" + fabrica + ", sn1=" + sn1 + ", sn2=" + sn2 + ", uuaa=" + uuaa);
        
        // 1. Obtener TODOS los nucleus filtrados directamente del repositorio (todas las páginas)
        List<Nucleus> allNucleusEntities = new ArrayList<>();
        int currentPage = 0;
        boolean hasMorePages = true;
        
        while (hasMorePages) {
            Page<Nucleus> nucleusPage = nucleusRepository.findByFilterFieldsPageable(vertical, fabrica, sn1, sn2, uuaa, 
                    PageRequest.of(currentPage, 1000));
            allNucleusEntities.addAll(nucleusPage.getContent());
            hasMorePages = nucleusPage.hasNext();
            currentPage++;
            System.out.println("Página " + currentPage + ": " + nucleusPage.getContent().size() + " nucleus encontrados");
        }
        
        System.out.println("Total nucleus encontrados para conteo: " + allNucleusEntities.size());
        
        // 2. Extraer los valores únicos para el conteo
        Set<String> fabricasSet = new HashSet<>();
        Set<String> sn1Set = new HashSet<>(); 
        Set<String> sn2Set = new HashSet<>();
        Set<String> uuaasSet = new HashSet<>();
        Set<Long> verticalesSet = new HashSet<>();
        
        for (Nucleus nucleus : allNucleusEntities) {
            // Contar fábricas (orgN2fabrica)
            if (nucleus.getOrgN2fabrica() != null && !nucleus.getOrgN2fabrica().trim().isEmpty()) {
                fabricasSet.add(nucleus.getOrgN2fabrica());
            }
            
            // Contar serviceN1 (sn1)
            if (nucleus.getServiceN1() != null && !nucleus.getServiceN1().trim().isEmpty()) {
                sn1Set.add(nucleus.getServiceN1());
            }
            
            // Contar serviceN2 (sn2)
            if (nucleus.getServiceN2() != null && !nucleus.getServiceN2().trim().isEmpty()) {
                sn2Set.add(nucleus.getServiceN2());
            }
            
            // Contar UUAAs (el campo uuaa puede contener múltiples valores separados por coma)
            if (nucleus.getUuaa() != null && !nucleus.getUuaa().trim().isEmpty()) {
                String[] uuaas = nucleus.getUuaa().split(",");
                for (String uuaaItem : uuaas) {
                    String trimmedUuaa = uuaaItem.trim();
                    if (!trimmedUuaa.isEmpty()) {
                        uuaasSet.add(trimmedUuaa);
                    }
                }
            }
            
            // Contar verticales diferentes asociadas a este nucleus
            if (nucleus.getVerticalNucleus() != null) {
                for (VerticalNucleus verticalNucleus : nucleus.getVerticalNucleus()) {
                    if (verticalNucleus.getVertical() != null && verticalNucleus.getVertical().getId() != null) {
                        verticalesSet.add(verticalNucleus.getVertical().getId());
                    }
                }
            }
        }
        
        // 3. Contar verticales diferentes que están asociadas a los nucleus filtrados
        int totalVerticales = verticalesSet.size();
        
        CountSummaryDTO countSummary = new CountSummaryDTO(
            totalVerticales,
            fabricasSet.size(),
            sn1Set.size(),
            sn2Set.size(),
            uuaasSet.size()
        );
        
        System.out.println("=== Resultado del conteo ===");
        System.out.println("Total Verticales: " + countSummary.getTotalVerticales());
        System.out.println("Total Fábricas: " + countSummary.getTotalFabricas());
        System.out.println("Total SN1: " + countSummary.getTotalSn1());
        System.out.println("Total SN2: " + countSummary.getTotalSn2());
        System.out.println("Total UUAAs: " + countSummary.getTotalUuaas());
        
        return countSummary;
    }
    
    /*
     * MÉTODO DESHABILITADO - Ya no se usa porque el endpoint ahora devuelve List<NucleusCoverageStatsSummary>
     * 
    public CoverageAverageDTO getAverageCoverage(String vertical, String uol2) {
        // Método comentado porque el endpoint cambió su funcionalidad
        return null;
    }
    */
    
    /**
     * Obtiene las estadísticas de coverage organizadas por niveles según los filtros
     * @param vertical Nombre de la vertical (opcional)
     * @param uol2 Nombre del UOL2/orgN2fabrica (opcional)
     * @param sn1 Nombre del SN1 (opcional)
     * @param sn2 Nombre del SN2 (opcional)
     * @param uuaa Nombre de la UUAA (opcional)
     * @return Lista de NucleusCoverageStatsSummary con los datos organizados por nivel
     */
    public List<NucleusCoverageStatsSummary> getCoverageStatsByLevel(String vertical, String uol2, String sn1, String sn2, String uuaa) {
        System.out.println("=== StatsSummaryService: Obteniendo estadísticas de coverage por nivel ===");
        System.out.println("Parámetros: vertical=" + vertical + ", uol2=" + uol2 + ", sn1=" + sn1 + ", sn2=" + sn2 + ", uuaa=" + uuaa);
        
        List<NucleusCoverageStatsSummary> result = new ArrayList<>();
        
        if (vertical == null || vertical.trim().isEmpty()) {
            // Sin filtros - devolver datos por VERTICAL
            System.out.println("Sin filtros - obteniendo datos por VERTICAL");
            List<Object[]> verticalData = sonarParamsRepository.getCoverageByVertical();
            
            for (Object[] row : verticalData) {
                if (row[0] != null && row[1] != null) {
                    String verticalName = row[0].toString();
                    Double coverage = Double.valueOf(row[1].toString());
                    
                    result.add(new NucleusCoverageStatsSummary(
                        verticalName, 
                        coverage, 
                        NucleusLevel.VERTICAL
                    ));
                    
                    System.out.println("Vertical: " + verticalName + " - Coverage: " + coverage + "%");
                }
            }
            
        } else if (uol2 == null || uol2.trim().isEmpty()) {
            // Solo vertical completa - devolver datos por UOL2
            System.out.println("Vertical completa - obteniendo datos por UOL2");
            List<Object[]> uol2Data = sonarParamsRepository.getCoverageByUol2ForVertical(vertical);
            
            for (Object[] row : uol2Data) {
                if (row[0] != null && row[1] != null) {
                    String uol2Name = row[0].toString();
                    Double coverage = Double.valueOf(row[1].toString());
                    
                    result.add(new NucleusCoverageStatsSummary(
                        uol2Name, 
                        coverage, 
                        NucleusLevel.UOL2
                    ));
                    
                    System.out.println("UOL2: " + uol2Name + " - Coverage: " + coverage + "%");
                }
            }
            
        } else if (sn1 == null || sn1.trim().isEmpty()) {
            // Vertical y UOL2 completos - devolver datos por SN1
            System.out.println("Vertical y UOL2 completos - obteniendo datos por SN1");
            List<Object[]> sn1Data = sonarParamsRepository.getCoverageBySn1ForVerticalAndUol2(vertical, uol2);
            
            for (Object[] row : sn1Data) {
                if (row[0] != null && row[1] != null) {
                    String sn1Name = row[0].toString();
                    Double coverage = Double.valueOf(row[1].toString());
                    
                    result.add(new NucleusCoverageStatsSummary(
                        sn1Name, 
                        coverage, 
                        NucleusLevel.SN1
                    ));
                    
                    System.out.println("SN1: " + sn1Name + " - Coverage: " + coverage + "%");
                }
            }
        } else if (sn2 == null || sn2.trim().isEmpty()) {
            // Vertical, UOL2 y SN1 completos - devolver datos por SN2
            System.out.println("Vertical, UOL2 y SN1 completos - obteniendo datos por SN2");
            List<Object[]> sn2Data = sonarParamsRepository.getCoverageBySn2ForVerticalUol2AndSn1(vertical, uol2, sn1);
            
            for (Object[] row : sn2Data) {
                if (row[0] != null && row[1] != null) {
                    String sn2Name = row[0].toString();
                    Double coverage = Double.valueOf(row[1].toString());
                    
                    result.add(new NucleusCoverageStatsSummary(
                        sn2Name, 
                        coverage, 
                        NucleusLevel.SN2
                    ));
                    
                    System.out.println("SN2: " + sn2Name + " - Coverage: " + coverage + "%");
                }
            }
        } else if (uuaa == null || uuaa.trim().isEmpty()) {
            // Vertical, UOL2, SN1 y SN2 completos - devolver datos por UUAA
            System.out.println("Vertical, UOL2, SN1 y SN2 completos - obteniendo datos por UUAA");
            List<Object[]> uuaaData = sonarParamsRepository.getCoverageByUuaaForVerticalUol2Sn1AndSn2(vertical, uol2, sn1, sn2);
            
            // Set para almacenar UUAAs únicas
            Set<String> uniqueUuaas = new HashSet<>();
            
            // Separar las UUAAs concatenadas
            for (Object[] row : uuaaData) {
                if (row[0] != null) {
                    for (Object uuaaItem : row) {
                        System.out.println("UUAA concatenada encontrada: " + uuaaItem);
                        if (!uuaaItem.toString().isEmpty()) {
                            uniqueUuaas.add(uuaaItem.toString());
                        }
                    }
                }
            }
            
            System.out.println("UUAAs únicas encontradas: " + uniqueUuaas.size());
            
            // Para cada UUAA única, obtener su coverage promedio
            for (String uuaaName : uniqueUuaas) {
                try {
                    Double coverage = sonarParamsRepository.getCoverageBySpecificUuaa(uuaaName);
                    if (coverage != null) {
                        result.add(new NucleusCoverageStatsSummary(
                            uuaaName, 
                            coverage, 
                            NucleusLevel.UUAA
                        ));
                        
                        System.out.println("UUAA: " + uuaaName + " - Coverage: " + coverage + "%");
                    } else {
                        System.out.println("UUAA: " + uuaaName + " - Sin datos de coverage");
                    }
                } catch (Exception e) {
                    System.out.println("Error al obtener coverage para UUAA: " + uuaaName + " - " + e.getMessage());
                }
            }
        } else {
            // Vertical, UOL2, SN1, SN2 y UUAA completos - devolver datos por APP
            System.out.println("Vertical, UOL2, SN1, SN2 y UUAA completos - obteniendo datos por APP");
            List<Object[]> appData = sonarParamsRepository.getCoverageByAppForUuaa(vertical, uol2, sn1, sn2, uuaa);
            
            for (Object[] row : appData) {
                if (row[0] != null && row[1] != null) {
                    String appName = row[0].toString();
                    Double coverage = Double.valueOf(row[1].toString());
                    
                    result.add(new NucleusCoverageStatsSummary(
                        appName, 
                        coverage, 
                        NucleusLevel.APP
                    ));
                    
                    System.out.println("APP: " + appName + " - Coverage: " + coverage + "%");
                }
            }
        }
        
        System.out.println("=== Resultado final ===");
        System.out.println("Total elementos encontrados: " + result.size());
        
        return result;
    }
}
