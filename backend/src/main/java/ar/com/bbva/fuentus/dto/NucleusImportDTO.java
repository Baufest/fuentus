package ar.com.bbva.fuentus.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.opencsv.bean.CsvBindByName;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NucleusImportDTO {
    
    @CsvBindByName(column = "Id Fullservice")
    @JsonProperty("Id Fullservice")
    private Long idFullservice;
    
    @CsvBindByName(column = "Service N1")
    private String serviceN1;
    
    @CsvBindByName(column = "Owner Id Service N1")
    private String ownerIdServiceN1;
    
    @CsvBindByName(column = "Owner Service N1")
    private String ownerServiceN1;
    
    @CsvBindByName(column = "Service N2")
    private String serviceN2;
    
    @CsvBindByName(column = "Service N2 Description")
    private String serviceN2description;
    
    @CsvBindByName(column = "Owner Id Service N2")
    private String ownerIdServiceN2;
    
    @CsvBindByName(column = "Owner Service N2")
    private String ownerServiceN2;
    
    @CsvBindByName(column = "Area")
    private String area;
    
    @CsvBindByName(column = "Org N1")
    private String orgN1;
    
    @CsvBindByName(column = "Owner Id Org N1")
    private String ownerIdOrgN1;
    
    @CsvBindByName(column = "Owner Org N1")
    private String ownerOrgN1;
    
    @CsvBindByName(column = "Org N2")
    private String orgN2fabrica;
    
    @CsvBindByName(column = "Owner Id Org N2")
    private String ownerIdOrgN2;
    
    @CsvBindByName(column = "Owner Org N2")
    private String ownerOrg2ftl;
    
    @CsvBindByName(column = "CFS")
    private String cfs;
    
    @CsvBindByName(column = "SaaS")
    private String saas;
    
    @CsvBindByName(column = "Disponibilidad/C. Negocio")
    private String disponibilidadCnegocio;
    
    @CsvBindByName(column = "Confidencialidad/Icc")
    private String confidencialidadIcc;
    
    @CsvBindByName(column = "Integridad")
    private String integridad;
    
    @CsvBindByName(column = "Autenticidad")
    private String autenticidad;
    
    @CsvBindByName(column = "Relevante Resolucion")
    private String relevanteResolucion;
    
    @CsvBindByName(column = "Categoria")
    private String categoria;
    
    @CsvBindByName(column = "UUAA")
    private String uuaa;
    
    @CsvBindByName(column = "Estado")
    private String estado;
    
    @CsvBindByName(column = "DESC_GLOBAL_RELS")
    private Integer descGlobalRels;
    
    @CsvBindByName(column = "ASC_GLOBAL_RELS")
    private String ascGlobalRels;
    
    @CsvBindByName(column = "DESC_REG_RELS")
    private String descRegRels;
    
    @CsvBindByName(column = "ASC_REG_RELS")
    private String ascRegRels;
    
    @CsvBindByName(column = "REL_TYPE")
    private String relType;
}