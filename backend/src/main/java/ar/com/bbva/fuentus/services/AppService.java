package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.AppDTO;
import ar.com.bbva.fuentus.dto.AppSummaryDTO;
import ar.com.bbva.fuentus.dto.ServerInfoDTO;
import ar.com.bbva.fuentus.dto.ServiceSummaryDTO;
import ar.com.bbva.fuentus.dto.UuaaSummaryDTO;
import ar.com.bbva.fuentus.entities.App;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.mapper.AppsMapper;
import ar.com.bbva.fuentus.repositories.AppsRepository;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author seba
 */
@Service
public class AppService {

    @Autowired
    private AppsRepository appsRepository;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private NucleusRepository nucleusRepository;

    public List<AppSummaryDTO> getAppsByUUAA(String uuaa) {
        List<AppSummaryDTO> listaApps = appsRepository.findAppSummaryByUuaa(uuaa);
        return listaApps;
    }

    public Page<AppSummaryDTO> getAppsByUUAAPageable(String uuaa, int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<AppSummaryDTO> appsPage = appsRepository.findAppSummaryByUuaaPageable(uuaa, pageable);

        appsPage.forEach(appSummary -> {
             List<ServerInfoDTO> servers = serverRepository.findServersByUuaa(appSummary.getName());
             appSummary.setServers(servers);
        });

        return appsPage;
    }

    public Page<AppSummaryDTO> getAppsByUUAAPageableWithSearch(String uuaa, int page, String search) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<AppSummaryDTO> appsPage = (search == null || search.trim().isEmpty())
                ? appsRepository.findAppSummaryByUuaaPageable(uuaa, pageable)
                : appsRepository.findAppSummaryByUuaaAndSearchPageable(uuaa, search.trim(), pageable);

        appsPage.forEach(appSummary -> {
            List<ServerInfoDTO> servers = serverRepository.findServersByName(appSummary.getName());
            appSummary.setServers(servers);
        });

        return appsPage;
    }

    public Page<AppDTO> searchWithFilters(String searchTerm, String vertical, String uol2, String sn1, String sn2, int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<App> appsPage = appsRepository.findAppsWithFilters(searchTerm, vertical, uol2, sn1, sn2, pageable);
        return appsPage.map(AppsMapper::mapApp2DTO);
    }

    /**
     * Método para obtener TODAS las aplicaciones de una UUAA sin paginación,
     * incluyendo información completa de servidores
     */
    public List<AppSummaryDTO> getAllAppsByUUAAComplete(String uuaa) {
        System.out.println("AppService.getAllAppsByUUAAComplete - Buscando apps para UUAA: " + uuaa);
        
        List<AppSummaryDTO> listaApps = appsRepository.findAppSummaryByUuaa(uuaa.substring(0,4).toUpperCase());
        System.out.println("AppService - Repository devolvió: " + listaApps.size() + " apps para UUAA: " + uuaa);
        
        // Agregar información de servidores a cada aplicación
        listaApps.forEach(appSummary -> {
            System.out.println("  Procesando app: " + appSummary.getName());
            List<ServerInfoDTO> servers = serverRepository.findServersByName(appSummary.getName());
            System.out.println("    Servidores encontrados: " + servers.size());
            appSummary.setServers(servers);
        });
        
        System.out.println("AppService - Devolviendo " + listaApps.size() + " apps completas para UUAA: " + uuaa);
        return listaApps;
    }

	public AppSummaryDTO getAppDetails(Long appId) {
		AppSummaryDTO app = appsRepository.findAppDetailsById(appId);
        if (app == null) {
            return null;
        }
        List<ServerInfoDTO> servers = serverRepository.findServersByUuaa(app.getName());
        app.setServers(servers);
        
        return app;
	}

    /**
     * Obtiene un resumen agregado de métricas para una UUAA específica.
     * Incluye total de apps, coverage promedio, vulnerabilidades totales y estado RFO.
     */
    public UuaaSummaryDTO getUuaaSummary(String uuaa) {
        String normalizedUuaa = uuaa.substring(0, Math.min(4, uuaa.length())).toUpperCase();
        
        List<AppSummaryDTO> apps = appsRepository.findAppSummaryByUuaa(normalizedUuaa);
        
        UuaaSummaryDTO summary = new UuaaSummaryDTO();
        summary.setUuaa(normalizedUuaa);
        summary.setTotalApps(apps.size());
        
        // Calcular coverage promedio
        double totalCoverage = 0;
        int appsWithCoverage = 0;
        long totalBugs = 0;
        long totalSastLow = 0, totalSastMedium = 0, totalSastHigh = 0;
        long totalScaLow = 0, totalScaMedium = 0, totalScaHigh = 0, totalScaCritical = 0;
        
        for (AppSummaryDTO app : apps) {
            // Coverage
            if (app.getCoverage() != null) {
                totalCoverage += app.getCoverage();
                appsWithCoverage++;
            }
            
            // Bugs
            if (app.getBugs() != null) {
                totalBugs += app.getBugs();
            }
            
            // SAST
            if (app.getChimeraSast() != null) {
                totalSastLow += app.getChimeraSast().getTotalLow() != null ? app.getChimeraSast().getTotalLow() : 0;
                totalSastMedium += app.getChimeraSast().getTotalMedium() != null ? app.getChimeraSast().getTotalMedium() : 0;
                totalSastHigh += app.getChimeraSast().getTotalHigh() != null ? app.getChimeraSast().getTotalHigh() : 0;
            }
            
            // SCA
            if (app.getChimeraSca() != null) {
                totalScaLow += app.getChimeraSca().getTotalLow() != null ? app.getChimeraSca().getTotalLow() : 0;
                totalScaMedium += app.getChimeraSca().getTotalMedium() != null ? app.getChimeraSca().getTotalMedium() : 0;
                totalScaHigh += app.getChimeraSca().getTotalHigh() != null ? app.getChimeraSca().getTotalHigh() : 0;
                totalScaCritical += app.getChimeraSca().getTotalCritical() != null ? app.getChimeraSca().getTotalCritical() : 0;
            }
        }
        
        summary.setAverageCoverage(appsWithCoverage > 0 ? totalCoverage / appsWithCoverage : 0.0);
        summary.setTotalBugs(totalBugs);
        summary.setTotalSastLow(totalSastLow);
        summary.setTotalSastMedium(totalSastMedium);
        summary.setTotalSastHigh(totalSastHigh);
        summary.setTotalScaLow(totalScaLow);
        summary.setTotalScaMedium(totalScaMedium);
        summary.setTotalScaHigh(totalScaHigh);
        summary.setTotalScaCritical(totalScaCritical);
        
        // Obtener información RFO del Nucleus
        List<Nucleus> nucleusList = nucleusRepository.findByUuaaLike("%" + normalizedUuaa + "%");
        if (!nucleusList.isEmpty()) {
            Nucleus nucleus = nucleusList.get(0);
            if (nucleus.getRfo() != null) {
                summary.setRfoId(nucleus.getRfo().getRfoId());
                summary.setRfoEstado(nucleus.getRfo().getEstadoRfo());
            }
        }
        
        return summary;
    }

    /**
     * Obtiene un resumen agregado de métricas para un Servicio con múltiples UUAAs.
     */
    public ServiceSummaryDTO getServiceSummary(List<String> uuaas, String serviceN1, String serviceN2, 
                                                String ownerServiceN1, Long rfoId, String rfoEstado) {
        ServiceSummaryDTO summary = new ServiceSummaryDTO();
        summary.setServiceN1(serviceN1);
        summary.setServiceN2(serviceN2);
        summary.setOwnerServiceN1(ownerServiceN1);
        summary.setUuaas(uuaas);
        summary.setRfoId(rfoId);
        summary.setRfoEstado(rfoEstado);
        
        // Recopilar todas las apps de todas las UUAAs
        List<AppSummaryDTO> allApps = new ArrayList<>();
        for (String uuaa : uuaas) {
            String normalizedUuaa = uuaa.substring(0, Math.min(4, uuaa.length())).toUpperCase();
            List<AppSummaryDTO> apps = appsRepository.findAppSummaryByUuaa(normalizedUuaa);
            allApps.addAll(apps);
        }
        
        summary.setTotalApps(allApps.size());
        
        // Calcular métricas agregadas
        double totalCoverage = 0;
        int appsWithCoverage = 0;
        long totalBugs = 0;
        long totalSastLow = 0;
        long totalSastMedium = 0;
        long totalSastHigh = 0;
        long totalScaLow = 0;
        long totalScaMedium = 0;
        long totalScaHigh = 0;
        long totalScaCritical = 0;
        
        for (AppSummaryDTO app : allApps) {
            if (app.getCoverage() != null) {
                totalCoverage += app.getCoverage();
                appsWithCoverage++;
            }
            if (app.getBugs() != null) {
                totalBugs += app.getBugs();
            }
            if (app.getChimeraSast() != null) {
                totalSastLow += app.getChimeraSast().getTotalLow() != null ? app.getChimeraSast().getTotalLow() : 0;
                totalSastMedium += app.getChimeraSast().getTotalMedium() != null ? app.getChimeraSast().getTotalMedium() : 0;
                totalSastHigh += app.getChimeraSast().getTotalHigh() != null ? app.getChimeraSast().getTotalHigh() : 0;
            }
            if (app.getChimeraSca() != null) {
                totalScaLow += app.getChimeraSca().getTotalLow() != null ? app.getChimeraSca().getTotalLow() : 0;
                totalScaMedium += app.getChimeraSca().getTotalMedium() != null ? app.getChimeraSca().getTotalMedium() : 0;
                totalScaHigh += app.getChimeraSca().getTotalHigh() != null ? app.getChimeraSca().getTotalHigh() : 0;
                totalScaCritical += app.getChimeraSca().getTotalCritical() != null ? app.getChimeraSca().getTotalCritical() : 0;
            }
        }
        
        summary.setAverageCoverage(appsWithCoverage > 0 ? totalCoverage / appsWithCoverage : 0.0);
        summary.setTotalBugs(totalBugs);
        summary.setTotalSastLow(totalSastLow);
        summary.setTotalSastMedium(totalSastMedium);
        summary.setTotalSastHigh(totalSastHigh);
        summary.setTotalScaLow(totalScaLow);
        summary.setTotalScaMedium(totalScaMedium);
        summary.setTotalScaHigh(totalScaHigh);
        summary.setTotalScaCritical(totalScaCritical);
        
        return summary;
    }

    /**
     * Obtiene apps paginadas de múltiples UUAAs con búsqueda opcional.
     */
    public Page<AppSummaryDTO> getAppsByMultipleUUAAsPageable(List<String> uuaas, int page, String search) {
        Pageable pageable = PageRequest.of(page, 20);
        
        // Normalizar UUAAs
        List<String> normalizedUuaas = uuaas.stream()
            .map(u -> u.substring(0, Math.min(4, u.length())).toUpperCase())
            .distinct()
            .collect(Collectors.toList());
        
        // Recopilar todas las apps
        List<AppSummaryDTO> allApps = new ArrayList<>();
        for (String uuaa : normalizedUuaas) {
            List<AppSummaryDTO> apps = appsRepository.findAppSummaryByUuaa(uuaa);
            allApps.addAll(apps);
        }
        
        // Filtrar por búsqueda si existe
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase().trim();
            allApps = allApps.stream()
                .filter(app -> app.getName() != null && app.getName().toLowerCase().contains(searchLower))
                .collect(Collectors.toList());
        }
        
        // Agregar información de servidores
        allApps.forEach(appSummary -> {
            List<ServerInfoDTO> servers = serverRepository.findServersByName(appSummary.getName());
            appSummary.setServers(servers);
        });
        
        // Paginar manualmente
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allApps.size());
        
        List<AppSummaryDTO> pageContent = start < allApps.size() 
            ? allApps.subList(start, end) 
            : new ArrayList<>();
        
        return new PageImpl<>(pageContent, pageable, allApps.size());
    }

    /**
     * Obtiene un resumen agregado de métricas para un servicio por su ID de Nucleus.
     */
    public ServiceSummaryDTO getServiceSummaryById(Long serviceId) {
        Nucleus nucleus = nucleusRepository.findById(serviceId).orElse(null);
        if (nucleus == null) {
            return null;
        }
        
        // Obtener lista de UUAAs del servicio
        List<String> uuaas = new ArrayList<>();
        if (nucleus.getUuaa() != null && !nucleus.getUuaa().isEmpty()) {
            String[] uuaaArray = nucleus.getUuaa().split(",");
            for (String uuaa : uuaaArray) {
                uuaas.add(uuaa.trim());
            }
        }
        
        // Obtener RFO info
        Long rfoId = null;
        String rfoEstado = null;
        if (nucleus.getRfo() != null) {
            rfoId = nucleus.getRfo().getRfoId();
            rfoEstado = nucleus.getRfo().getEstadoRfo();
        }
        
        // Reusar el método existente
        ServiceSummaryDTO summary = getServiceSummary(
            uuaas, 
            nucleus.getServiceN1(), 
            nucleus.getServiceN2(), 
            nucleus.getOwnerServiceN1(), 
            rfoId, 
            rfoEstado
        );
        
        summary.setServiceId(serviceId);
        
        return summary;
    }

    /**
     * Obtiene apps paginadas para un servicio por su ID de Nucleus.
     */
    public Page<AppSummaryDTO> getAppsByServiceIdPageable(Long serviceId, int page, String search) {
        Nucleus nucleus = nucleusRepository.findById(serviceId).orElse(null);
        if (nucleus == null) {
            return Page.empty();
        }
        
        // Obtener lista de UUAAs del servicio
        List<String> uuaas = new ArrayList<>();
        if (nucleus.getUuaa() != null && !nucleus.getUuaa().isEmpty()) {
            String[] uuaaArray = nucleus.getUuaa().split(",");
            for (String uuaa : uuaaArray) {
                uuaas.add(uuaa.trim());
            }
        }
        
        // Reusar el método existente
        return getAppsByMultipleUUAAsPageable(uuaas, page, search);
    }
}