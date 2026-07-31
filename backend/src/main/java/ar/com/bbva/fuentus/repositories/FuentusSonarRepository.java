package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.FuentusSonar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FuentusSonarRepository extends JpaRepository<FuentusSonar, Long> {
    
    /**
     * Obtiene el promedio de coverage para una vertical específica
     * Relaciona FuentusSonar -> App (por repo/repoUrl) -> Nucleus (por UUAA) -> Vertical
     */
    @Query(value = "SELECT AVG(CASE " +
            "WHEN fs.coverage REGEXP '^[0-9]+\\.?[0-9]*%?$' " +
            "THEN CAST(REPLACE(fs.coverage, '%', '') AS DECIMAL(5,2)) " +
            "ELSE NULL END) as average_coverage, " +
            "COUNT(DISTINCT fs.repo) as total_projects " +
            "FROM fuentus_sonar fs " +
            "INNER JOIN app a ON (LOWER(fs.repo) = LOWER(a.name) OR fs.repo_url = a.bitbucket_url) " +
            "INNER JOIN nucleus_services n ON a.uuaa = SUBSTRING(n.UUAA, 1, 4) " +
            "INNER JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "INNER JOIN verticales v ON vn.vertical_id = v.id " +
            "WHERE v.name = :vertical " +
            "AND fs.coverage IS NOT NULL " +
            "AND fs.coverage != ''",
            nativeQuery = true)
    Object[] getAverageCoverageByVertical(@Param("vertical") String vertical);
    
    /**
     * Obtiene el promedio de coverage para un UOL2 específico
     * Relaciona FuentusSonar -> App (por repo/repoUrl) -> Nucleus (por UUAA y orgN2fabrica)
     */
    @Query(value = "SELECT AVG(CASE " +
            "WHEN fs.coverage REGEXP '^[0-9]+\\.?[0-9]*%?$' " +
            "THEN CAST(REPLACE(fs.coverage, '%', '') AS DECIMAL(5,2)) " +
            "ELSE NULL END) as average_coverage, " +
            "COUNT(DISTINCT fs.repo) as total_projects " +
            "FROM fuentus_sonar fs " +
            "INNER JOIN app a ON (LOWER(fs.repo) = LOWER(a.name) OR fs.repo_url = a.bitbucket_url) " +
            "INNER JOIN nucleus_services n ON a.uuaa = SUBSTRING(n.UUAA, 1, 4) " +
            "WHERE n.Org_N2_Fabrica = :uol2 " +
            "AND fs.coverage IS NOT NULL " +
            "AND fs.coverage != ''",
            nativeQuery = true)
    Object[] getAverageCoverageByUol2(@Param("uol2") String uol2);
    
    /**
     * Obtiene el promedio general de coverage de todos los proyectos
     */
    @Query(value = "SELECT AVG(CASE " +
            "WHEN fs.coverage REGEXP '^[0-9]+\\.?[0-9]*%?$' " +
            "THEN CAST(REPLACE(fs.coverage, '%', '') AS DECIMAL(5,2)) " +
            "ELSE NULL END) as average_coverage, " +
            "COUNT(DISTINCT fs.repo) as total_projects " +
            "FROM fuentus_sonar fs " +
            "WHERE fs.coverage IS NOT NULL " +
            "AND fs.coverage != ''",
            nativeQuery = true)
    Object[] getGeneralAverageCoverage();
    
    /**
     * Obtiene el coverage promedio por vertical cuando no hay filtros
     */
    @Query(value = "SELECT v.name as vertical_name, " +
            "AVG(CASE " +
            "WHEN fs.coverage REGEXP '^[0-9]+\\.?[0-9]*%?$' " +
            "THEN CAST(REPLACE(fs.coverage, '%', '') AS DECIMAL(5,2)) " +
            "ELSE NULL END) as average_coverage " +
            "FROM fuentus_sonar fs " +
            "INNER JOIN app a ON (LOWER(fs.repo) = LOWER(a.name) OR fs.repo_url = a.bitbucket_url) " +
            "INNER JOIN nucleus_services n ON a.uuaa = SUBSTRING(n.UUAA, 1, 4) " +
            "INNER JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "INNER JOIN verticales v ON vn.vertical_id = v.id " +
            "WHERE fs.coverage IS NOT NULL " +
            "AND fs.coverage != '' " +
            "GROUP BY v.name " +
            "ORDER BY v.name",
            nativeQuery = true)
    java.util.List<Object[]> getCoverageByVertical();
    
    /**
     * Obtiene el coverage promedio por UOL2 para una vertical específica
     */
    @Query(value = "SELECT n.Org_N2_Fabrica as uol2_name, " +
            "AVG(CASE " +
            "WHEN fs.coverage REGEXP '^[0-9]+\\.?[0-9]*%?$' " +
            "THEN CAST(REPLACE(fs.coverage, '%', '') AS DECIMAL(5,2)) " +
            "ELSE NULL END) as average_coverage " +
            "FROM fuentus_sonar fs " +
            "INNER JOIN app a ON (LOWER(fs.repo) = LOWER(a.name) OR fs.repo_url = a.bitbucket_url) " +
            "INNER JOIN nucleus_services n ON a.uuaa = SUBSTRING(n.UUAA, 1, 4) " +
            "INNER JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "INNER JOIN verticales v ON vn.vertical_id = v.id " +
            "WHERE v.name = :vertical " +
            "AND fs.coverage IS NOT NULL " +
            "AND fs.coverage != '' " +
            "AND n.Org_N2_Fabrica IS NOT NULL " +
            "GROUP BY n.Org_N2_Fabrica " +
            "ORDER BY n.Org_N2_Fabrica",
            nativeQuery = true)
    java.util.List<Object[]> getCoverageByUol2ForVertical(@Param("vertical") String vertical);
    
    /**
     * Obtiene el coverage promedio por ServiceN1 para una vertical y UOL2 específicos
     */
    @Query(value = "SELECT n.Service_N1 as sn1_name, " +
            "AVG(CASE " +
            "WHEN fs.coverage REGEXP '^[0-9]+\\.?[0-9]*%?$' " +
            "THEN CAST(REPLACE(fs.coverage, '%', '') AS DECIMAL(5,2)) " +
            "ELSE NULL END) as average_coverage " +
            "FROM fuentus_sonar fs " +
            "INNER JOIN app a ON (LOWER(fs.repo) = LOWER(a.name) OR fs.repo_url = a.bitbucket_url) " +
            "INNER JOIN nucleus_services n ON a.uuaa = SUBSTRING(n.UUAA, 1, 4) " +
            "INNER JOIN vertical_nucleus_services vn ON n.Id_Fullservice = vn.nucleus_id_fullservice " +
            "INNER JOIN verticales v ON vn.vertical_id = v.id " +
            "WHERE v.name = :vertical " +
            "AND n.Org_N2_Fabrica = :uol2 " +
            "AND fs.coverage IS NOT NULL " +
            "AND fs.coverage != '' " +
            "AND n.Service_N1 IS NOT NULL " +
            "GROUP BY n.Service_N1 " +
            "ORDER BY n.Service_N1",
            nativeQuery = true)
    java.util.List<Object[]> getCoverageBySn1ForVerticalAndUol2(@Param("vertical") String vertical, @Param("uol2") String uol2);
}
