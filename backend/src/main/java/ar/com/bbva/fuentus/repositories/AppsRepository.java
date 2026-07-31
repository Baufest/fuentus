/*
 */
package ar.com.bbva.fuentus.repositories;


import ar.com.bbva.fuentus.dto.AppSummaryDTO;
import ar.com.bbva.fuentus.entities.App;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author o002349
 */
public interface AppsRepository extends JpaRepository<App, Long>, JpaSpecificationExecutor<App> {

    App findBySonarUrl(String url);

    App findBySonar9Url(String url);

    List<App> findByUuaa(String uuaa);

    // Método para buscar por combinación name + projectId (criterio de unicidad para import)
    App findByNameAndProjectId(String name, String projectId);

    @Query(value = "SELECT new ar.com.bbva.fuentus.dto.AppSummaryDTO(" +
            "a.id, " +
            "a.name, " +
            "a.uuaa, " +
            "a.bitbucketUrl, " +
            "a.sonarUrl, " +
            "a.sonar10Url, " +
            "sp.bugs, " +
            "sp.coverage, " +
            "a.monolith, " +
            "cr.totalHigh, " +
            "cr.totalMedium, " +
            "cr.totalLow, " +
            "cr.language, " +
            "a.chimeraUrl, " +
            "a.samuelUrl, " +
            "sca.high, " +
            "sca.medium, " +
            "sca.low, " +
            "sca.critical) " +
            "FROM App a " +
            "LEFT JOIN SonarParam sp ON sp.appId = a.id AND sp.analisisDate = (" +
            "SELECT MAX(sp2.analisisDate) " +
            "FROM SonarParam sp2 " +
            "WHERE sp2.appId = a.id) " +
            "LEFT JOIN  ChimeraReview cr ON cr.appId = a.id AND cr.endDate = (" +
            "SELECT MAX(cr2.endDate) " +
            "FROM ChimeraReview cr2 " +
            "WHERE cr2.appId = a.id) " +
            "LEFT JOIN ChimeraSca sca ON sca.appId = a.id " +
    //        "WHERE a.uuaa = :uuaa")
            "WHERE a.uuaa LIKE CONCAT(:uuaa, '%')")
    List<AppSummaryDTO> findAppSummaryByUuaa(@Param("uuaa") String uuaa);

    @Query(value = "SELECT new ar.com.bbva.fuentus.dto.AppSummaryDTO(" +
            "a.id, " +
            "a.name, " +
            "a.uuaa, " +
            "a.bitbucketUrl, " +
            "a.sonarUrl, " +
            "a.sonar10Url, " +
            "sp.bugs, " +
            "sp.coverage, " +
            "a.monolith, " +
            "cr.totalHigh, " +
            "cr.totalMedium, " +
            "cr.totalLow, " +
            "cr.language, " +
            "a.chimeraUrl, " +
            "a.samuelUrl, " +
            "sca.high, " +
            "sca.medium, " +
            "sca.low, " +
            "sca.critical) " +
            "FROM App a " +
            "LEFT JOIN SonarParam sp ON sp.appId = a.id AND sp.analisisDate = (" +
            "SELECT MAX(sp2.analisisDate) " +
            "FROM SonarParam sp2 " +
            "WHERE sp2.appId = a.id) " +
            "LEFT JOIN  ChimeraReview cr ON cr.appId = a.id AND cr.endDate = (" +
            "SELECT MAX(cr2.endDate) " +
            "FROM ChimeraReview cr2 " +
            "WHERE cr2.appId = a.id) " +
            "LEFT JOIN ChimeraSca sca ON sca.appId = a.id " +
            "WHERE a.uuaa LIKE CONCAT(:uuaa, '%')")
    Page<AppSummaryDTO> findAppSummaryByUuaaPageable(@Param("uuaa") String uuaa, Pageable pageable);

    @Query(value = "SELECT new ar.com.bbva.fuentus.dto.AppSummaryDTO(" +
            "a.id, " +
            "a.name, " +
            "a.uuaa, " +
            "a.bitbucketUrl, " +
            "a.sonarUrl, " +
            "a.sonar10Url, " +
            "sp.bugs, " +
            "sp.coverage, " +
            "a.monolith, " +
            "cr.totalHigh, " +
            "cr.totalMedium, " +
            "cr.totalLow, " +
            "cr.language, " +
            "a.chimeraUrl, " +
            "a.samuelUrl, " +
            "sca.high, " +
            "sca.medium, " +
            "sca.low, " +
            "sca.critical) " +
            "FROM App a " +
            "LEFT JOIN SonarParam sp ON sp.appId = a.id AND sp.analisisDate = (" +
            "SELECT MAX(sp2.analisisDate) " +
            "FROM SonarParam sp2 " +
            "WHERE sp2.appId = a.id) " +
            "LEFT JOIN  ChimeraReview cr ON cr.appId = a.id AND cr.endDate = (" +
            "SELECT MAX(cr2.endDate) " +
            "FROM ChimeraReview cr2 " +
            "WHERE cr2.appId = a.id) " +
            "LEFT JOIN ChimeraSca sca ON sca.appId = a.id " +
            "WHERE a.uuaa LIKE CONCAT(:uuaa, '%') AND LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<AppSummaryDTO> findAppSummaryByUuaaAndSearchPageable(@Param("uuaa") String uuaa, @Param("search") String search, Pageable pageable);

    @Query(value = "SELECT DISTINCT a.* FROM app a " +
            "LEFT JOIN nucleus_services n ON a.nucleus_id_fullservice = n.Id_Fullservice " +
            "LEFT JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "LEFT JOIN verticales v ON vn.vertical_id = v.id " +
            "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.uuaa) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:vertical IS NULL OR :vertical = '' OR v.name = :vertical) " +
            "AND (:uol2 IS NULL OR :uol2 = '' OR n.Org_N2_Fabrica = :uol2) " +
            "AND (:sn1 IS NULL OR :sn1 = '' OR n.Service_N1 = :sn1) " +
            "AND (:sn2 IS NULL OR :sn2 = '' OR n.Service_N2 = :sn2)",
            countQuery = "SELECT COUNT(DISTINCT a.id) FROM app a " +
            "LEFT JOIN nucleus_services n ON a.nucleus_id_fullservice = n.Id_Fullservice " +
            "LEFT JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "LEFT JOIN verticales v ON vn.vertical_id = v.id " +
            "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.uuaa) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:vertical IS NULL OR :vertical = '' OR v.name = :vertical) " +
            "AND (:uol2 IS NULL OR :uol2 = '' OR n.Org_N2_Fabrica = :uol2) " +
            "AND (:sn1 IS NULL OR :sn1 = '' OR n.Service_N1 = :sn1) " +
            "AND (:sn2 IS NULL OR :sn2 = '' OR n.Service_N2 = :sn2)",
            nativeQuery = true)
    Page<App> findAppsWithFilters(
            @Param("searchTerm") String searchTerm,
            @Param("vertical") String vertical,
            @Param("uol2") String uol2,
            @Param("sn1") String sn1,
            @Param("sn2") String sn2,
            Pageable pageable);

                @Query(value = "SELECT new ar.com.bbva.fuentus.dto.AppSummaryDTO(" +
                        "a.id, " +
                        "a.name, " +
                        "a.uuaa, " +
            "a.bitbucketUrl, " +
            "a.sonarUrl, " +
            "a.sonar10Url, " +
            "sp.bugs, " +
            "sp.coverage, " +
            "a.monolith, " +
            "cr.totalHigh, " +
            "cr.totalMedium, " +
            "cr.totalLow, " +
            "cr.language, " +
            "a.chimeraUrl, " +
            "a.samuelUrl, " +
            "sca.high, " +
            "sca.medium, " +
            "sca.low, " +
            "sca.critical) " +
            "FROM App a " +
            "LEFT JOIN SonarParam sp ON sp.appId = a.id AND sp.analisisDate = (" +
            "SELECT MAX(sp2.analisisDate) " +
            "FROM SonarParam sp2 " +
            "WHERE sp2.appId = a.id) " +
            "LEFT JOIN  ChimeraReview cr ON cr.appId = a.id AND cr.endDate = (" +
            "SELECT MAX(cr2.endDate) " +
            "FROM ChimeraReview cr2 " +
            "WHERE cr2.appId = a.id) " +
            "LEFT JOIN ChimeraSca sca ON sca.appId = a.id " +
            "WHERE a.id = :appId")
    AppSummaryDTO findAppDetailsById(Long appId);
}
