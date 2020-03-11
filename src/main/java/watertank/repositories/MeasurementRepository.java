package watertank.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import watertank.models.Measurement;

import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    @Query("SELECT m FROM Measurement m ORDER BY m.id DESC")
    List<Measurement> findLatestXRecords(PageRequest pageRequest);
}
