/*
 */
package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.SonarParam;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author o002349
 */
public interface SonarParamsRepository extends JpaRepository<SonarParam, Long> {

    SonarParam findFirstByAppIdAndAnalisisDateLessThanEqualOrderByAnalisisDateDesc(Long appId, Date analisisDate);

    @Query(value = "SELECT * FROM sonar_params WHERE app_id = :appId ORDER BY analisis_date DESC LIMIT 1", nativeQuery = true)
    SonarParam findLatestByAppId(@Param("appId") Long appId);
    
    /**
     * Obtiene el coverage promedio por vertical usando el último análisis de cada aplicación
     */
    @Query(value = "SELECT v.name as vertical_name, " +
            "AVG(sp.coverage) as average_coverage " +
            "FROM sonar_params sp " +
            "INNER JOIN app a ON sp.app_id = a.id " +
            "INNER JOIN nucleus_services n ON a.uuaa = SUBSTRING(n.UUAA, 1, 4) " +
            "INNER JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "INNER JOIN verticales v ON vn.vertical_id = v.id " +
            "INNER JOIN ( " +
            "    SELECT app_id, MAX(analisis_date) as max_date " +
            "    FROM sonar_params " +
            "    GROUP BY app_id " +
            ") latest ON sp.app_id = latest.app_id AND sp.analisis_date = latest.max_date " +
            "WHERE sp.coverage IS NOT NULL " +
            "GROUP BY v.name " +
            "ORDER BY v.name",
            nativeQuery = true)
    java.util.List<Object[]> getCoverageByVertical();
    
    /**
     * Obtiene el coverage promedio por UOL2 para una vertical específica usando el último análisis
     */
    @Query(value = "SELECT n.Org_N2_Fabrica as uol2_name, " +
            "AVG(sp.coverage) as average_coverage " +
            "FROM sonar_params sp " +
            "INNER JOIN app a ON sp.app_id = a.id " +
            "INNER JOIN nucleus_services n ON a.uuaa = SUBSTRING(n.UUAA, 1, 4) " +
            "INNER JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "INNER JOIN verticales v ON vn.vertical_id = v.id " +
            "INNER JOIN ( " +
            "    SELECT app_id, MAX(analisis_date) as max_date " +
            "    FROM sonar_params " +
            "    GROUP BY app_id " +
            ") latest ON sp.app_id = latest.app_id AND sp.analisis_date = latest.max_date " +
            "WHERE v.name = :vertical " +
            "AND sp.coverage IS NOT NULL " +
            "AND n.Org_N2_Fabrica IS NOT NULL " +
            "GROUP BY n.Org_N2_Fabrica " +
            "ORDER BY n.Org_N2_Fabrica",
            nativeQuery = true)
    java.util.List<Object[]> getCoverageByUol2ForVertical(@Param("vertical") String vertical);
    
    /**
     * Obtiene el coverage promedio por ServiceN1 para una vertical y UOL2 específicos usando el último análisis
     */
    @Query(value = "SELECT n.Service_N1 as sn1_name, " +
            "AVG(sp.coverage) as average_coverage " +
            "FROM sonar_params sp " +
            "INNER JOIN app a ON sp.app_id = a.id " +
            "INNER JOIN nucleus_services n ON a.uuaa = SUBSTRING(n.UUAA, 1, 4) " +
            "INNER JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "INNER JOIN verticales v ON vn.vertical_id = v.id " +
            "INNER JOIN ( " +
            "    SELECT app_id, MAX(analisis_date) as max_date " +
            "    FROM sonar_params " +
            "    GROUP BY app_id " +
            ") latest ON sp.app_id = latest.app_id AND sp.analisis_date = latest.max_date " +
            "WHERE v.name = :vertical " +
            "AND n.Org_N2_Fabrica = :uol2 " +
            "AND sp.coverage IS NOT NULL " +
            "AND n.Service_N1 IS NOT NULL " +
            "GROUP BY n.Service_N1 " +
            "ORDER BY n.Service_N1",
            nativeQuery = true)
    java.util.List<Object[]> getCoverageBySn1ForVerticalAndUol2(@Param("vertical") String vertical, @Param("uol2") String uol2);
    
    /**
     * Obtiene el coverage promedio por ServiceN2 para una vertical, UOL2 y ServiceN1 específicos usando el último análisis
     */
    @Query(value = "SELECT n.Service_N2 as sn2_name, " +
            "AVG(sp.coverage) as average_coverage " +
            "FROM sonar_params sp " +
            "INNER JOIN app a ON sp.app_id = a.id " +
            "INNER JOIN nucleus_services n ON a.uuaa = SUBSTRING(n.UUAA, 1, 4) " +
            "INNER JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "INNER JOIN verticales v ON vn.vertical_id = v.id " +
            "INNER JOIN ( " +
            "    SELECT app_id, MAX(analisis_date) as max_date " +
            "    FROM sonar_params " +
            "    GROUP BY app_id " +
            ") latest ON sp.app_id = latest.app_id AND sp.analisis_date = latest.max_date " +
            "WHERE v.name = :vertical " +
            "AND n.Org_N2_Fabrica = :uol2 " +
            "AND n.Service_N1 = :sn1 " +
            "AND sp.coverage IS NOT NULL " +
            "AND n.Service_N2 IS NOT NULL " +
            "GROUP BY n.Service_N2 " +
            "ORDER BY n.Service_N2",
            nativeQuery = true)
    java.util.List<Object[]> getCoverageBySn2ForVerticalUol2AndSn1(@Param("vertical") String vertical, @Param("uol2") String uol2, @Param("sn1") String sn1);
    
    /**
     * Obtiene los datos de nucleus con sus UUAAs concatenadas para una vertical, UOL2, ServiceN1 y ServiceN2 específicos
     * Las UUAAs se separarán posteriormente en el servicio
     */
    @Query(value = "SELECT n.UUAA as uuaa_concatenated " +
            "FROM nucleus_services n " +
            "INNER JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "INNER JOIN verticales v ON vn.vertical_id = v.id " +
            "WHERE v.name = :vertical " +
            "AND n.Org_N2_Fabrica = :uol2 " +
            "AND n.Service_N1 = :sn1 " +
            "AND n.Service_N2 = :sn2 " +
            "AND n.UUAA IS NOT NULL " +
            "AND TRIM(n.UUAA) != ''",
            nativeQuery = true)
    java.util.List<Object[]> getCoverageByUuaaForVerticalUol2Sn1AndSn2(@Param("vertical") String vertical, @Param("uol2") String uol2, @Param("sn1") String sn1, @Param("sn2") String sn2);
    
    /**
     * Obtiene el coverage promedio para una UUAA específica usando el último análisis
     * Considera que las UUAAs en nucleus_services pueden estar separadas por comas
     */
    @Query(value = "SELECT AVG(sp.coverage) as average_coverage " +
            "FROM sonar_params sp " +
            "INNER JOIN app a ON sp.app_id = a.id " +
            "INNER JOIN nucleus_services n ON (n.UUAA = :uuaa OR n.UUAA LIKE CONCAT(:uuaa, ',%') OR n.UUAA LIKE CONCAT('%,', :uuaa, ',%') OR n.UUAA LIKE CONCAT('%,', :uuaa)) " +
            "INNER JOIN ( " +
            "    SELECT app_id, MAX(analisis_date) as max_date " +
            "    FROM sonar_params " +
            "    GROUP BY app_id " +
            ") latest ON sp.app_id = latest.app_id AND sp.analisis_date = latest.max_date " +
            "WHERE a.uuaa = SUBSTRING(:uuaa, 1, 4) " +
            "AND sp.coverage IS NOT NULL",
            nativeQuery = true)
    Double getCoverageBySpecificUuaa(@Param("uuaa") String uuaa);
    
    /**
     * Obtiene el coverage por aplicación para una UUAA específica usando el último análisis
     * Considera que las UUAAs en nucleus_services pueden estar separadas por comas
     */
    @Query(value = "SELECT a.name as app_name, " +
            "sp.coverage as coverage " +
            "FROM sonar_params sp " +
            "INNER JOIN app a ON sp.app_id = a.id " +
            "INNER JOIN nucleus_services n ON (n.UUAA = :uuaa OR n.UUAA LIKE CONCAT(:uuaa, ',%') OR n.UUAA LIKE CONCAT('%,', :uuaa, ',%') OR n.UUAA LIKE CONCAT('%,', :uuaa)) " +
            "INNER JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "INNER JOIN verticales v ON vn.vertical_id = v.id " +
            "INNER JOIN ( " +
            "    SELECT app_id, MAX(analisis_date) as max_date " +
            "    FROM sonar_params " +
            "    GROUP BY app_id " +
            ") latest ON sp.app_id = latest.app_id AND sp.analisis_date = latest.max_date " +
            "WHERE v.name = :vertical " +
            "AND n.Org_N2_Fabrica = :uol2 " +
            "AND n.Service_N1 = :sn1 " +
            "AND n.Service_N2 = :sn2 " +
            "AND a.uuaa = SUBSTRING(:uuaa, 1, 4) " +
            "AND sp.coverage IS NOT NULL " +
            "AND a.name IS NOT NULL " +
            "ORDER BY a.name",
            nativeQuery = true)
    java.util.List<Object[]> getCoverageByAppForUuaa(@Param("vertical") String vertical, @Param("uol2") String uol2, @Param("sn1") String sn1, @Param("sn2") String sn2, @Param("uuaa") String uuaa);
}

