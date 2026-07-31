package ar.com.bbva.fuentus.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "nucleus_services")
public class Nucleus implements Serializable {

    @Id
    @Column(name = "Id_Fullservice")
    private Long id;

    @Column(name = "Service_N1")
    private String serviceN1;

    @Column(name = "Owner_Id_Service_N1")
    private String ownerIdServiceN1;

    @Column(name = "Owner_Service_N1")
    private String ownerServiceN1;

    @Column(name = "Service_N2")
    private String serviceN2;

    @Column(name = "Service_N2_Description")
    private String serviceN2description;

    @Column(name = "Owner_Id_Service_N2")
    private String ownerIdServiceN2;

    @Column(name = "Owner_Service_N2")
    private String ownerServiceN2;

    private String area;

    @Column(name = "Org_N1")
    private String orgN1;

    @Column(name = "Owner_Id_Org_N1")
    private String ownerIdOrgN1;

    @Column(name = "Owner_Org_N1")
    private String ownerOrgN1;

    @Column(name = "Org_N2_Fabrica")
    private String orgN2fabrica;

    @Column(name = "Owner_Id_Org_N2")
    private String ownerIdOrgN2;

    @Column(name = "Owner_Org_N2_ftl")
    private String ownerOrg2ftl;

    @Column(name = "CFS")
    private String cfs;

    @Column(name = "SaaS")
    private String saas;

    @Column(name = "Disponibilidad_C_Negocio")
    private String disponibilidadCnegocio;

    //@Column(name = "Confidencialidad/Icc")
    @Column(name = "Confidencialidad_Icc")
    private String confidencialidadIcc;

    @Column(name = "Integridad")
    private String integridad;

    @Column(name = "Autenticidad")
    private String autenticidad;

    @Column(name = "Relevante_Resolucion")
    private String relevanteResolucion;

    @Column(name = "Categoria")
    private String categoria;

    @Column(name = "UUAA")
    private String uuaa;

    @Column(name = "Estado")
    private String estado;

    @Column(name = "DESC_GLOBAL_RELS")
    private Integer descGlobalRels;

    @Column(name = "ASC_GLOBAL_RELS")
    private String ascGlobalRels;

    @Column(name = "DESC_REG_RELS")
    private String descRegRels;

    @Column(name = "ASC_REG_RELS")
    private String ascRegRels;

    @Column(name = "REL_TYPE")
    private String relType;

    @OneToMany(mappedBy = "nucleus", fetch = FetchType.LAZY)
    private List<VerticalNucleus> verticalNucleus;

    @OneToMany(mappedBy = "nucleus", fetch = FetchType.LAZY)
    private List<App> apps;

    // Relación OneToOne con Rfo basada en nucleus_id
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "nucleus")
    private Rfo rfo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nucleus nucleus = (Nucleus) o;
        return Objects.equals(id, nucleus.id) &&
               Objects.equals(serviceN1, nucleus.serviceN1) &&
               Objects.equals(ownerIdServiceN1, nucleus.ownerIdServiceN1) &&
               Objects.equals(ownerServiceN1, nucleus.ownerServiceN1) &&
               Objects.equals(serviceN2, nucleus.serviceN2) &&
               Objects.equals(serviceN2description, nucleus.serviceN2description) &&
               Objects.equals(ownerIdServiceN2, nucleus.ownerIdServiceN2) &&
               Objects.equals(ownerServiceN2, nucleus.ownerServiceN2) &&
               Objects.equals(area, nucleus.area) &&
               Objects.equals(orgN1, nucleus.orgN1) &&
               Objects.equals(ownerIdOrgN1, nucleus.ownerIdOrgN1) &&
               Objects.equals(ownerOrgN1, nucleus.ownerOrgN1) &&
               Objects.equals(orgN2fabrica, nucleus.orgN2fabrica) &&
               Objects.equals(ownerIdOrgN2, nucleus.ownerIdOrgN2) &&
               Objects.equals(ownerOrg2ftl, nucleus.ownerOrg2ftl) &&
               Objects.equals(cfs, nucleus.cfs) &&
               Objects.equals(saas, nucleus.saas) &&
               Objects.equals(disponibilidadCnegocio, nucleus.disponibilidadCnegocio) &&
               Objects.equals(confidencialidadIcc, nucleus.confidencialidadIcc) &&
               Objects.equals(integridad, nucleus.integridad) &&
               Objects.equals(autenticidad, nucleus.autenticidad) &&
               Objects.equals(relevanteResolucion, nucleus.relevanteResolucion) &&
               Objects.equals(categoria, nucleus.categoria) &&
               Objects.equals(uuaa, nucleus.uuaa) &&
               Objects.equals(estado, nucleus.estado) &&
               Objects.equals(descGlobalRels, nucleus.descGlobalRels) &&
               Objects.equals(ascGlobalRels, nucleus.ascGlobalRels) &&
               Objects.equals(descRegRels, nucleus.descRegRels) &&
               Objects.equals(ascRegRels, nucleus.ascRegRels) &&
               Objects.equals(relType, nucleus.relType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceN1, ownerIdServiceN1, ownerServiceN1, serviceN2, serviceN2description,
                           ownerIdServiceN2, ownerServiceN2, area, orgN1, ownerIdOrgN1, ownerOrgN1, orgN2fabrica,
                           ownerIdOrgN2, ownerOrg2ftl, cfs, saas, disponibilidadCnegocio, confidencialidadIcc,
                           integridad, autenticidad, relevanteResolucion, categoria, uuaa, estado, descGlobalRels,
                           ascGlobalRels, descRegRels, ascRegRels, relType);
    }

    @Override
    public String toString() {
        return "Nucleus{" +
               "id=" + id +
               ", serviceN1='" + serviceN1 + '\'' +
               ", ownerIdServiceN1='" + ownerIdServiceN1 + '\'' +
               ", ownerServiceN1='" + ownerServiceN1 + '\'' +
               ", serviceN2='" + serviceN2 + '\'' +
               ", serviceN2description='" + serviceN2description + '\'' +
               ", ownerIdServiceN2='" + ownerIdServiceN2 + '\'' +
               ", ownerServiceN2='" + ownerServiceN2 + '\'' +
               ", area='" + area + '\'' +
               ", orgN1='" + orgN1 + '\'' +
               ", ownerIdOrgN1='" + ownerIdOrgN1 + '\'' +
               ", ownerOrgN1='" + ownerOrgN1 + '\'' +
               ", orgN2fabrica='" + orgN2fabrica + '\'' +
               ", ownerIdOrgN2='" + ownerIdOrgN2 + '\'' +
               ", ownerOrg2ftl='" + ownerOrg2ftl + '\'' +
               ", cfs='" + cfs + '\'' +
               ", saas='" + saas + '\'' +
               ", disponibilidadCnegocio='" + disponibilidadCnegocio + '\'' +
               ", confidencialidadIcc='" + confidencialidadIcc + '\'' +
               ", integridad='" + integridad + '\'' +
               ", autenticidad='" + autenticidad + '\'' +
               ", relevanteResolucion='" + relevanteResolucion + '\'' +
               ", categoria='" + categoria + '\'' +
               ", uuaa='" + uuaa + '\'' +
               ", estado='" + estado + '\'' +
               ", descGlobalRels=" + descGlobalRels +
               ", ascGlobalRels='" + ascGlobalRels + '\'' +
               ", descRegRels='" + descRegRels + '\'' +
               ", ascRegRels='" + ascRegRels + '\'' +
               ", relType='" + relType + '\'' +
               '}';
    }
}
