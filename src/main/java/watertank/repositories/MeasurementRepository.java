package watertank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import watertank.models.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

}
