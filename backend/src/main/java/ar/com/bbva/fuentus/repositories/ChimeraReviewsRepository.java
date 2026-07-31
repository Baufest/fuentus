/*
 */
package ar.com.bbva.fuentus.repositories;

import ar.com.bbva.fuentus.entities.ChimeraReview;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author o002349
 */
public interface ChimeraReviewsRepository extends JpaRepository<ChimeraReview, Long> {

    ChimeraReview findFirstByAppIdOrderByEndDateDesc(Long appId);

    ChimeraReview findFirstByAppIdAndStatusAndEndDateLessThanEqualOrderByEndDateDesc(Long appId, String status, Date endDate);
    
    List<ChimeraReview> findByAppIdAndProjectId(Long appId, String projectId);

}
