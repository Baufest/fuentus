package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.dto.FactoryVertical;
import ar.com.bbva.fuentus.entities.Nucleus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NucleusRepository extends JpaRepository<Nucleus, Long> {

    List<Nucleus> findByUuaaLike(String uuaa);

    Optional<Nucleus> findByServiceN2(String serviceN2);

    @Query(value = "SELECT * FROM nucleus_services WHERE " +
            "Service_N1 LIKE %:searchTerm% OR " +
           // "Owner_Id_Service_N1 LIKE %:searchTerm% OR " +
            "Owner_Service_N1 LIKE %:searchTerm% OR " +
            "Service_N2 LIKE %:searchTerm% OR " +
           // "Service_N2_Description LIKE %:searchTerm% OR " +
           // "Owner_Id_Service_N2  LIKE %:searchTerm% OR " +
//                "Owner_Service_N2 LIKE %:searchTerm% OR " +
//                "area LIKE %:searchTerm% OR " +
//                "Org_N1 LIKE %:searchTerm% OR " +
//                "Owner_Id_Org_N1 LIKE %:searchTerm% OR " +
//                "Owner_Org_N1 LIKE %:searchTerm% OR " +
//                "Org_N2_Fabrica LIKE %:searchTerm% OR " +
//                "Owner_Id_Org_N2 LIKE %:searchTerm% OR " +
//                "Owner_Org_N2_ftl LIKE %:searchTerm% OR " +
//                "CFS LIKE %:searchTerm% OR " +
//                "SaaS LIKE %:searchTerm% OR " +
//                "Disponibilidad_C_Negocio LIKE %:searchTerm% OR " +
//                "Confidencialidad_Icc LIKE %:searchTerm% OR " +
//                "Integridad LIKE %:searchTerm% OR " +
//                "Autenticidad LIKE %:searchTerm% OR " +
//                "Relevante_Resolucion LIKE %:searchTerm% OR " +
//                "Categoria LIKE %:searchTerm% OR " +
             "UUAA LIKE %:searchTerm%",
//                "Estado LIKE %:searchTerm% OR " +
//                "DESC_GLOBAL_RELS LIKE %:searchTerm% OR " +
//                "ASC_GLOBAL_RELS LIKE %:searchTerm% OR " +
//                "DESC_REG_RELS LIKE %:searchTerm% OR " +
//                "ASC_REG_RELS LIKE %:searchTerm% OR " +
//                "REL_TYPE LIKE %:searchTerm%" ,
            nativeQuery = true)
    public List<Nucleus> findByAnyFieldContaining(@Param("searchTerm") String searchTerm);

    @Query(value = "SELECT * FROM nucleus_services WHERE " +
            "Service_N1 LIKE %:searchTerm% OR " +
            "Owner_Service_N1 LIKE %:searchTerm% OR " +
            "Service_N2 LIKE %:searchTerm% OR " +
            "Owner_Service_N2 LIKE %:searchTerm% OR " +
            "UUAA LIKE %:searchTerm%",
            nativeQuery = true)
    public Page<Nucleus> findByAnyFieldContainingPageable(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT n FROM Nucleus n WHERE " +
            "(:area IS NULL OR :area = '' OR n.area = :area) AND " +
            "(:orgN1 IS NULL OR :orgN1 = '' OR n.orgN1 = :orgN1) AND " +
            "(:orgN2fabrica IS NULL OR :orgN2fabrica = '' OR n.orgN2fabrica = :orgN2fabrica)")
    Page<Nucleus> findByOptionalFieldsPageable(@Param("area") String area,
                                               @Param("orgN1") String orgN1,
                                               @Param("orgN2fabrica") String orgN2fabrica,
                                               Pageable pageable);

    @Query(value = "SELECT DISTINCT n.* FROM nucleus_services n " +
           "LEFT JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
           "LEFT JOIN verticales v ON vn.vertical_id = v.id " +
           "WHERE " +
           "(:vertical IS NULL OR :vertical = '' OR v.name = :vertical) AND " +
           "(:fabrica IS NULL OR :fabrica = '' OR n.Org_N2_Fabrica = :fabrica) AND " +
           "(:sn1 IS NULL OR :sn1 = '' OR n.Service_N1 = :sn1) AND " +
           "(:sn2 IS NULL OR :sn2 = '' OR n.Service_N2 = :sn2) AND " +
           "(:uuaa IS NULL OR :uuaa = '' OR n.UUAA LIKE CONCAT('%', :uuaa, '%'))",
           countQuery = "SELECT COUNT(DISTINCT n.Id_Fullservice) FROM nucleus_services n " +
           "LEFT JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
           "LEFT JOIN verticales v ON vn.vertical_id = v.id " +
           "WHERE " +
           "(:vertical IS NULL OR :vertical = '' OR v.name = :vertical) AND " +
           "(:fabrica IS NULL OR :fabrica = '' OR n.Org_N2_Fabrica = :fabrica) AND " +
           "(:sn1 IS NULL OR :sn1 = '' OR n.Service_N1 = :sn1) AND " +
           "(:sn2 IS NULL OR :sn2 = '' OR n.Service_N2 = :sn2) AND " +
           "(:uuaa IS NULL OR :uuaa = '' OR n.UUAA LIKE CONCAT('%', :uuaa, '%'))",
           nativeQuery = true)
    Page<Nucleus> findByFilterFieldsPageable(@Param("vertical") String vertical,
                                             @Param("fabrica") String fabrica,
                                             @Param("sn1") String sn1,
                                             @Param("sn2") String sn2,
                                             @Param("uuaa") String uuaa,
                                             Pageable pageable);

    /**
     * Busca todas las entidades Nucleus que tengan orgN2fabrica en la lista proporcionada
     */
    List<Nucleus> findByOrgN2fabricaIn(List<String> orgN2fabricaList);
    
    /**
     * Obtiene todos los valores distintos de orgN2 con sus verticales asociadas (incluye orgN2 sin verticales)
     */
    @Query("SELECT DISTINCT new ar.com.bbva.fuentus.dto.FactoryVertical(n.orgN2fabrica, v) FROM Nucleus n " +
           "LEFT JOIN n.verticalNucleus vn " +
           "LEFT JOIN vn.vertical v " +
           "WHERE n.orgN2fabrica IS NOT NULL " +
           "ORDER BY n.orgN2fabrica, v.name")
    List<FactoryVertical> findDistinctOrgN2WithVerticales();
    
    // Methods for data import functionality
    
    /**
     * Find Nucleus by serviceN1 and serviceN2 combination
     * This is used to identify unique services for upsert operations
     */
    Optional<Nucleus> findByServiceN1AndServiceN2(String serviceN1, String serviceN2);
    
    /**
     * Find all Nucleus entries by serviceN1
     */
    List<Nucleus> findByServiceN1(String serviceN1);
    
    /**
     * Find all Nucleus entries by area
     */
    List<Nucleus> findByArea(String area);
    
    /**
     * Find all Nucleus entries by estado (status)
     */
    @Query("SELECT n FROM Nucleus n WHERE n.estado = :estado")
    List<Nucleus> findByEstado(@Param("estado") String estado);
    
    /**
     * Find all Nucleus entries by category
     */
    List<Nucleus> findByCategoria(String categoria);

    /**
     * Find first Nucleus entry by serviceN2 (for RFO association)
     * Returns the first match since we expect only one
     */
    Optional<Nucleus> findFirstByServiceN2(String serviceN2);

    /**
     * Obtiene todos los valores distintos de UOL2 desde la tabla Apps relacionada por UUAA
     */
    @Query(value = "SELECT DISTINCT n.Org_N2_Fabrica FROM nucleus_services n " +
           "WHERE n.Org_N2_Fabrica IS NOT NULL " +
           "ORDER BY n.Org_N2_Fabrica",
           nativeQuery = true)
    List<String> findDistinctUol2Values();

    /**
     * Obtiene todos los valores distintos de UOL2 filtrados por vertical
     */
    @Query(value = "SELECT DISTINCT n.Org_N2_Fabrica FROM nucleus_services n " +
           "JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
           "JOIN verticales v ON vn.vertical_id = v.id " +
           "WHERE v.name = :vertical AND n.Org_N2_Fabrica IS NOT NULL " +
           "ORDER BY n.Org_N2_Fabrica",
           nativeQuery = true)
    List<String> findDistinctUol2ValuesByVertical(@Param("vertical") String vertical);

    /**
     * Obtiene todos los valores distintos de Service N1
     */
    @Query(value = "SELECT DISTINCT n.Service_N1 FROM nucleus_services n " +
           "WHERE n.Service_N1 IS NOT NULL AND n.Service_N1 != '' " +
           "ORDER BY n.Service_N1", 
           nativeQuery = true)
    List<String> findDistinctSn1Values();

    /**
     * Obtiene todos los valores distintos de Service N1 filtrados por vertical
     */
    @Query(value = "SELECT DISTINCT n.Service_N1 FROM nucleus_services n " +
           "JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
           "JOIN verticales v ON vn.vertical_id = v.id " +
           "WHERE v.name = :vertical AND n.Service_N1 IS NOT NULL AND n.Service_N1 != '' " +
           "ORDER BY n.Service_N1", 
           nativeQuery = true)
    List<String> findDistinctSn1ValuesByVertical(@Param("vertical") String vertical);

    /**
     * Obtiene todos los valores distintos de Service N2
     */
    @Query(value = "SELECT DISTINCT n.Service_N2 FROM nucleus_services n " +
           "WHERE n.Service_N2 IS NOT NULL AND n.Service_N2 != '' " +
           "ORDER BY n.Service_N2", 
           nativeQuery = true)
    List<String> findDistinctSn2Values();

    /**
     * Obtiene todos los valores distintos de Service N2 filtrados por vertical
     */
    @Query(value = "SELECT DISTINCT n.Service_N2 FROM nucleus_services n " +
           "JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
           "JOIN verticales v ON vn.vertical_id = v.id " +
           "WHERE v.name = :vertical AND n.Service_N2 IS NOT NULL AND n.Service_N2 != '' " +
           "ORDER BY n.Service_N2", 
           nativeQuery = true)
    List<String> findDistinctSn2ValuesByVertical(@Param("vertical") String vertical);

    /**
     * Obtiene valores distintos de UOL2 con filtros dinámicos
     */
    @Query(value = "SELECT DISTINCT n.Org_N2_Fabrica FROM nucleus_services n " +
           "LEFT JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
           "LEFT JOIN verticales v ON vn.vertical_id = v.id " +
           "WHERE n.Org_N2_Fabrica IS NOT NULL " +
           "AND (:vertical IS NULL OR :vertical = '' OR v.name = :vertical) " +
           "ORDER BY n.Org_N2_Fabrica", 
           nativeQuery = true)
    List<String> findDistinctUol2ValuesWithFilters(@Param("vertical") String vertical);

    /**
     * Obtiene valores distintos de SN1 con filtros dinámicos
     */
    @Query(value = "SELECT DISTINCT n.Service_N1 FROM nucleus_services n " +
           "LEFT JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
           "LEFT JOIN verticales v ON vn.vertical_id = v.id " +
           "WHERE n.Service_N1 IS NOT NULL AND n.Service_N1 != '' " +
           "AND (:vertical IS NULL OR :vertical = '' OR v.name = :vertical) " +
           "AND (:uol2 IS NULL OR :uol2 = '' OR n.Org_N2_Fabrica = :uol2) " +
           "ORDER BY n.Service_N1", 
           nativeQuery = true)
    List<String> findDistinctSn1ValuesWithFilters(@Param("vertical") String vertical, @Param("uol2") String uol2);

    /**
     * Obtiene valores distintos de SN2 con filtros dinámicos
     */
    @Query(value = "SELECT DISTINCT n.Service_N2 FROM nucleus_services n " +
           "LEFT JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
           "LEFT JOIN verticales v ON vn.vertical_id = v.id " +
           "WHERE n.Service_N2 IS NOT NULL AND n.Service_N2 != '' " +
           "AND (:vertical IS NULL OR :vertical = '' OR v.name = :vertical) " +
           "AND (:uol2 IS NULL OR :uol2 = '' OR n.Org_N2_Fabrica = :uol2) " +
           "AND (:sn1 IS NULL OR :sn1 = '' OR n.Service_N1 = :sn1) " +
           "ORDER BY n.Service_N2", 
           nativeQuery = true)
    List<String> findDistinctSn2ValuesWithFilters(@Param("vertical") String vertical, @Param("uol2") String uol2, @Param("sn1") String sn1);
}





