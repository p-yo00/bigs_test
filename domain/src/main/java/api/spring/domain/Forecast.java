package api.spring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Forecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate baseDate;
    private LocalTime baseTime;
    private String category;
    private double nx;
    private double ny;
    private double obsrValue;

    /**
     * map을 forecast 객체로 변환하여 리턴하는 static 함수
     */
    public static Forecast mapToForecast(Map<String, Object> forecastMap) {
        return Forecast.builder()
                .baseDate(LocalDate.parse((String)forecastMap.get("baseDate"),
                        DateTimeFormatter.ofPattern("yyyyMMdd")))
                .baseTime(LocalTime.parse((String)forecastMap.get("baseTime"),
                        DateTimeFormatter.ofPattern("HHmm")))
                .category((String)forecastMap.get("category"))
                .nx((double)forecastMap.get("nx"))
                .ny((double)forecastMap.get("ny"))
                .obsrValue(Double.parseDouble((String)forecastMap.get(
                        "obsrValue")))
                .build();
    }
}
