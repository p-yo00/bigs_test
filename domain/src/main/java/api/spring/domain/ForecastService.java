package api.spring.domain;

import api.spring.apicall.CallAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ForecastService {
    private final ForecastRepository forecastRepository;
    private final CallAPIService callAPIService;

    @Value("${target.lat}")
    private String lat;
    @Value("${target.lnt}")
    private String lnt;

    /**
     * 기상청 단기예보 API를 가져온다.
     * -> 현재 날짜와 시간, properties에 저장된 '문충로'의 위경도를 기준으로 가져온다.
     */
    @Transactional
    public void callApi() {
        List<Map<String, Object>> forecastMap =
                callAPIService.callApi(lat, lnt, LocalDateTime.now());
        List<Forecast> forecasts = new ArrayList<>();

        for (Map<String, Object> item : forecastMap) {
            forecasts.add(Forecast.mapToForecast(item));
        }
        forecastRepository.saveAll(forecasts);
    }

    /**
     * DB에서 현재 날짜와 시간대에 저장된 단기 예보가 있으면 가져온다.
     */
    @Transactional
    public List<Forecast> getForecast(LocalDateTime now) {
        LocalDate date = now.toLocalDate();
        LocalTime time = LocalTime.of(now.toLocalTime().getHour(), 0);
        return forecastRepository.findByBaseDateAndBaseTimeAndNxAndNy(
                date, time, Double.parseDouble(lat), Double.parseDouble(lnt)
        );
    }
}
