package ar.com.bbva.fuentus.mappers;

import ar.com.bbva.fuentus.dto.*;
import ar.com.bbva.fuentus.entities.*;
import org.springframework.stereotype.Component;

@Component
public class DataImportMapper {

    // NUCLEUS MAPPING METHODS
    
    /**
     * Convert NucleusImportDTO to Nucleus entity
     */
    public Nucleus toNucleus(NucleusImportDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // Validación estricta: Id_Fullservice es OBLIGATORIO
        if (dto.getIdFullservice() == null) {
            throw new IllegalArgumentException("Id_Fullservice es obligatorio y no puede ser null");
        }
        
        if (dto.getIdFullservice() <= 0) {
            throw new IllegalArgumentException("Id_Fullservice debe ser un número positivo, recibido: " + dto.getIdFullservice());
        }
        
        Nucleus nucleus = new Nucleus();
        nucleus.setId(dto.getIdFullservice()); // Mapea idFullservice → id
        nucleus.setServiceN1(dto.getServiceN1());
        nucleus.setOwnerIdServiceN1(dto.getOwnerIdServiceN1());
        nucleus.setOwnerServiceN1(dto.getOwnerServiceN1());
        nucleus.setServiceN2(dto.getServiceN2());
        nucleus.setServiceN2description(dto.getServiceN2description());
        nucleus.setOwnerIdServiceN2(dto.getOwnerIdServiceN2());
        nucleus.setOwnerServiceN2(dto.getOwnerServiceN2());
        nucleus.setArea(dto.getArea());
        nucleus.setOrgN1(dto.getOrgN1());
        nucleus.setOwnerIdOrgN1(dto.getOwnerIdOrgN1());
        nucleus.setOwnerOrgN1(dto.getOwnerOrgN1());
        nucleus.setOrgN2fabrica(dto.getOrgN2fabrica());
        nucleus.setOwnerIdOrgN2(dto.getOwnerIdOrgN2());
        nucleus.setOwnerOrg2ftl(dto.getOwnerOrg2ftl());
        nucleus.setCfs(dto.getCfs());
        nucleus.setSaas(dto.getSaas());
        nucleus.setDisponibilidadCnegocio(dto.getDisponibilidadCnegocio());
        nucleus.setConfidencialidadIcc(dto.getConfidencialidadIcc());
        nucleus.setIntegridad(dto.getIntegridad());
        nucleus.setAutenticidad(dto.getAutenticidad());
        nucleus.setRelevanteResolucion(dto.getRelevanteResolucion());
        nucleus.setCategoria(dto.getCategoria());
        nucleus.setUuaa(dto.getUuaa());
        nucleus.setEstado(dto.getEstado());
        nucleus.setDescGlobalRels(dto.getDescGlobalRels());
        nucleus.setAscGlobalRels(dto.getAscGlobalRels());
        nucleus.setDescRegRels(dto.getDescRegRels());
        nucleus.setAscRegRels(dto.getAscRegRels());
        nucleus.setRelType(dto.getRelType());
        
        return nucleus;
    }

    /**
     * Update existing Nucleus entity with data from NucleusImportDTO
     */
    public void updateNucleusFromDTO(NucleusImportDTO dto, Nucleus nucleus) {
        if (dto == null || nucleus == null) {
            return;
        }
        
        // Validación: El Id_Fullservice del DTO debe coincidir con el de la entidad existente
        if (dto.getIdFullservice() == null) {
            throw new IllegalArgumentException("Id_Fullservice es obligatorio para UPDATE");
        }
        
        if (!dto.getIdFullservice().equals(nucleus.getId())) {
            throw new IllegalArgumentException("No se puede cambiar el Id_Fullservice. ID actual: " + nucleus.getId() + 
                                             ", ID recibido: " + dto.getIdFullservice());
        }
        
        // Update all fields except ID (which should remain unchanged)
        nucleus.setServiceN1(dto.getServiceN1());
        nucleus.setOwnerIdServiceN1(dto.getOwnerIdServiceN1());
        nucleus.setOwnerServiceN1(dto.getOwnerServiceN1());
        nucleus.setServiceN2(dto.getServiceN2());
        nucleus.setServiceN2description(dto.getServiceN2description());
        nucleus.setOwnerIdServiceN2(dto.getOwnerIdServiceN2());
        nucleus.setOwnerServiceN2(dto.getOwnerServiceN2());
        nucleus.setArea(dto.getArea());
        nucleus.setOrgN1(dto.getOrgN1());
        nucleus.setOwnerIdOrgN1(dto.getOwnerIdOrgN1());
        nucleus.setOwnerOrgN1(dto.getOwnerOrgN1());
        nucleus.setOrgN2fabrica(dto.getOrgN2fabrica());
        nucleus.setOwnerIdOrgN2(dto.getOwnerIdOrgN2());
        nucleus.setOwnerOrg2ftl(dto.getOwnerOrg2ftl());
        nucleus.setCfs(dto.getCfs());
        nucleus.setSaas(dto.getSaas());
        nucleus.setDisponibilidadCnegocio(dto.getDisponibilidadCnegocio());
        nucleus.setConfidencialidadIcc(dto.getConfidencialidadIcc());
        nucleus.setIntegridad(dto.getIntegridad());
        nucleus.setAutenticidad(dto.getAutenticidad());
        nucleus.setRelevanteResolucion(dto.getRelevanteResolucion());
        nucleus.setCategoria(dto.getCategoria());
        nucleus.setUuaa(dto.getUuaa());
        nucleus.setEstado(dto.getEstado());
        nucleus.setDescGlobalRels(dto.getDescGlobalRels());
        nucleus.setAscGlobalRels(dto.getAscGlobalRels());
        nucleus.setDescRegRels(dto.getDescRegRels());
        nucleus.setAscRegRels(dto.getAscRegRels());
        nucleus.setRelType(dto.getRelType());
    }

    // APP MAPPING METHODS
    
    /**
     * Convert AppImportDTO to App entity
     */
    public App toApp(AppImportDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // Validación: name y projectId son obligatorios
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("name es obligatorio y no puede ser null o vacío");
        }
        
        if (dto.getProjectId() == null || dto.getProjectId().trim().isEmpty()) {
            throw new IllegalArgumentException("projectId es obligatorio y no puede ser null o vacío");
        }
        
        App app = new App();
        // No setear ID - será auto-generado
        app.setName(dto.getName());
        app.setProjectId(dto.getProjectId());
        app.setVertical(dto.getVertical());
        app.setFolder(dto.getFolder());
        app.setBitbucketUrl(dto.getBitbucketUrl());
        app.setSonarUrl(dto.getSonarUrl());
        app.setSonar9Url(dto.getSonar9Url());
        app.setSonar10Url(dto.getSonar10Url());
        app.setSamuelUrl(dto.getSamuelUrl());
        app.setChimeraUrl(dto.getChimeraUrl());
        app.setObsolete(dto.getObsolete());
        app.setObsoleteBy(dto.getObsoleteBy());
        app.setUuaa(dto.getUuaa());
        app.setSn1(dto.getSn1());
        app.setSn2(dto.getSn2());
        app.setSo(dto.getSo());
        app.setAo(dto.getAo());
        app.setStratosUol1(dto.getStratosUol1());
        app.setStratosUol2(dto.getStratosUol2());
        app.setStratosSl1(dto.getStratosSl1());
        app.setStratosSl1Owner(dto.getStratosSl1Owner());
        app.setStratosSl2(dto.getStratosSl2());
        app.setStratosSl2Owner(dto.getStratosSl2Owner());
        app.setJava(dto.getJava());
        app.setUnitTest(dto.getUnitTest());
        app.setJest(dto.getJest());
        app.setMonolith(dto.getMonolith());
        app.setConfig(dto.getConfig());
        app.setSonarKey(dto.getSonarKey());
        app.setVtravk(dto.getVtravk());
        app.setCriticalLocal(dto.getCriticalLocal());
        app.setNodeVersion(dto.getNodeVersion());
        app.setCriticalAudit(dto.getCriticalAudit());
        app.setCriticalConfidential(dto.getCriticalConfidential());
        app.setCriticalFraud(dto.getCriticalFraud());
        app.setCriticalCfs(dto.getCriticalCfs());
        app.setExtra(dto.getExtra());
        app.setArchitecture(dto.getArchitecture());
        app.setSource(dto.getSource());
        
        return app;
    }

    /**
     * Update existing App entity with data from AppImportDTO
     */
    public void updateAppFromDTO(AppImportDTO dto, App app) {
        if (dto == null || app == null) {
            return;
        }
        
        // Validación: Los campos de identificación deben coincidir
        if (dto.getName() == null || !dto.getName().equals(app.getName())) {
            throw new IllegalArgumentException("No se puede cambiar el name. Name actual: " + app.getName() + 
                                             ", Name recibido: " + dto.getName());
        }
        
        if (dto.getProjectId() == null || !dto.getProjectId().equals(app.getProjectId())) {
            throw new IllegalArgumentException("No se puede cambiar el projectId. ProjectId actual: " + app.getProjectId() + 
                                             ", ProjectId recibido: " + dto.getProjectId());
        }
        
        // Update all fields except ID, name and projectId (which should remain unchanged)
        app.setVertical(dto.getVertical());
        app.setFolder(dto.getFolder());
        app.setBitbucketUrl(dto.getBitbucketUrl());
        app.setSonarUrl(dto.getSonarUrl());
        app.setSonar9Url(dto.getSonar9Url());
        app.setSonar10Url(dto.getSonar10Url());
        app.setSamuelUrl(dto.getSamuelUrl());
        app.setChimeraUrl(dto.getChimeraUrl());
        app.setObsolete(dto.getObsolete());
        app.setObsoleteBy(dto.getObsoleteBy());
        app.setUuaa(dto.getUuaa());
        app.setSn1(dto.getSn1());
        app.setSn2(dto.getSn2());
        app.setSo(dto.getSo());
        app.setAo(dto.getAo());
        app.setStratosUol1(dto.getStratosUol1());
        app.setStratosUol2(dto.getStratosUol2());
        app.setStratosSl1(dto.getStratosSl1());
        app.setStratosSl1Owner(dto.getStratosSl1Owner());
        app.setStratosSl2(dto.getStratosSl2());
        app.setStratosSl2Owner(dto.getStratosSl2Owner());
        app.setJava(dto.getJava());
        app.setUnitTest(dto.getUnitTest());
        app.setJest(dto.getJest());
        app.setMonolith(dto.getMonolith());
        app.setConfig(dto.getConfig());
        app.setSonarKey(dto.getSonarKey());
        app.setVtravk(dto.getVtravk());
        app.setCriticalLocal(dto.getCriticalLocal());
        app.setNodeVersion(dto.getNodeVersion());
        app.setCriticalAudit(dto.getCriticalAudit());
        app.setCriticalConfidential(dto.getCriticalConfidential());
        app.setCriticalFraud(dto.getCriticalFraud());
        app.setCriticalCfs(dto.getCriticalCfs());
        app.setExtra(dto.getExtra());
        app.setArchitecture(dto.getArchitecture());
        app.setSource(dto.getSource());
    }

    // RFO MAPPING METHODS
    
    /**
     * Convert RfoImportDTO to Rfo entity
     */
    public Rfo toRfo(RfoImportDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // Validación: rfoId es obligatorio
        if (dto.getRfoId() == null) {
            throw new IllegalArgumentException("RFO ID es obligatorio y no puede ser null");
        }
        
        if (dto.getRfoId() <= 0) {
            throw new IllegalArgumentException("RFO ID debe ser un número positivo, recibido: " + dto.getRfoId());
        }
        
        Rfo rfo = new Rfo();
        rfo.setRfoId(dto.getRfoId()); // ID proporcionado, no auto-generado
        rfo.setEmail(dto.getEmail());
        rfo.setEstadoRfo(dto.getEstadoRfo());
        rfo.setFechaPuestaProduccion(dto.getFechaPuestaProduccion());
        // nucleus será seteado por el processor después de buscar por sevicioN2
        
        return rfo;
    }

    /**
     * Update existing Rfo entity with data from RfoImportDTO
     */
    public void updateRfoFromDTO(RfoImportDTO dto, Rfo rfo) {
        if (dto == null || rfo == null) {
            return;
        }
        
        // Validación: El RFO ID del DTO debe coincidir con el de la entidad existente
        if (dto.getRfoId() == null) {
            throw new IllegalArgumentException("RFO ID es obligatorio para UPDATE");
        }
        
        if (!dto.getRfoId().equals(rfo.getRfoId())) {
            throw new IllegalArgumentException("No se puede cambiar el RFO ID. ID actual: " + rfo.getRfoId() + 
                                             ", ID recibido: " + dto.getRfoId());
        }
        
        // Update all fields except rfoId (which should remain unchanged)
        rfo.setEmail(dto.getEmail());
        rfo.setEstadoRfo(dto.getEstadoRfo());
        rfo.setFechaPuestaProduccion(dto.getFechaPuestaProduccion());
        // nucleus será actualizado por el processor después de buscar por sevicioN2
    }

    // TODO: Add mapping methods for other entities when DTOs are created
    // - CHIMERA REVIEWS MAPPING
    // - CHIMERA SAST MAPPING  
}