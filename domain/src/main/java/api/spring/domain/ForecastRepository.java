package api.spring.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {
    List<Forecast> findByBaseDateAndBaseTimeAndNxAndNy(LocalDate baseDate,
                                                       LocalTime baseTime,
                                                       double nx,
                                                       double ny);
}
