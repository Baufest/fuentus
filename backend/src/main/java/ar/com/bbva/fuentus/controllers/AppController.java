package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.AppDTO;
import ar.com.bbva.fuentus.dto.AppSummaryDTO;
import ar.com.bbva.fuentus.dto.ServiceSummaryDTO;
import ar.com.bbva.fuentus.dto.UuaaSummaryDTO;
import ar.com.bbva.fuentus.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/apps")
@RestController
public class AppController {

    @Autowired
    private AppService appsService;

    @GetMapping("/{uuaa}")
    public ResponseEntity<Page<AppSummaryDTO>> getAppsByUUAAPageable(
            @PathVariable String uuaa, 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search) {
        
        Page<AppSummaryDTO> apps = appsService.getAppsByUUAAPageableWithSearch(uuaa, page, search);
        
        return ResponseEntity.ok(apps);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AppDTO>> searchApps(
            @RequestParam(required = false, name = "q") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String vertical,
            @RequestParam(required = false) String uol2,
            @RequestParam(required = false) String sn1,
            @RequestParam(required = false) String sn2) {
        Page<AppDTO> appsPage = appsService.searchWithFilters(searchTerm, vertical, uol2, sn1, sn2, page);
        return ResponseEntity.ok(appsPage);
    }

    @GetMapping("/details/{appId}")
    public ResponseEntity<AppSummaryDTO> getAppDetails(@PathVariable Long appId) {
        AppSummaryDTO appDetails = appsService.getAppDetails(appId);
        return ResponseEntity.ok(appDetails);
    }

    @GetMapping("/{uuaa}/summary")
    public ResponseEntity<UuaaSummaryDTO> getUuaaSummary(@PathVariable String uuaa) {
        UuaaSummaryDTO summary = appsService.getUuaaSummary(uuaa);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/service/summary")
    public ResponseEntity<ServiceSummaryDTO> getServiceSummary(
            @RequestParam List<String> uuaas,
            @RequestParam(required = false) String serviceN1,
            @RequestParam(required = false) String serviceN2,
            @RequestParam(required = false) String ownerServiceN1,
            @RequestParam(required = false) Long rfoId,
            @RequestParam(required = false) String rfoEstado) {
        ServiceSummaryDTO summary = appsService.getServiceSummary(uuaas, serviceN1, serviceN2, ownerServiceN1, rfoId, rfoEstado);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/by-uuaas")
    public ResponseEntity<Page<AppSummaryDTO>> getAppsByMultipleUUAAs(
            @RequestParam List<String> uuaas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search) {
        Page<AppSummaryDTO> apps = appsService.getAppsByMultipleUUAAsPageable(uuaas, page, search);
        return ResponseEntity.ok(apps);
    }

    @GetMapping("/service/{serviceId}/summary")
    public ResponseEntity<ServiceSummaryDTO> getServiceSummaryById(@PathVariable Long serviceId) {
        ServiceSummaryDTO summary = appsService.getServiceSummaryById(serviceId);
        if (summary == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/service/{serviceId}/apps")
    public ResponseEntity<Page<AppSummaryDTO>> getAppsByServiceId(
            @PathVariable Long serviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search) {
        Page<AppSummaryDTO> apps = appsService.getAppsByServiceIdPageable(serviceId, page, search);
        return ResponseEntity.ok(apps);
    }
}