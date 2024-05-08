package api.spring.application;

import api.spring.domain.Forecast;
import api.spring.domain.ForecastService;
import api.spring.exception.ApiDataNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@PropertySource({"classpath:api-application.properties",
        "classpath:domain-application.properties"})
public class ApiController {
    private final ForecastService forecastService;

    /**
     * 현재 시간을 기준으로 단기 예보 API를 불러와서 db에 저장
     */
    @PostMapping("/forecast")
    public HttpStatus createForecast() {
        forecastService.callApi();
        return HttpStatus.OK;
    }

    /**
     * 현재 날짜/시간 기준 단기 예보 조회
     */
    @GetMapping("/forecast")
    public List<Forecast> getForecast() {
        List<Forecast> forecasts =
                forecastService.getForecast(LocalDateTime.now());
        if (forecasts.isEmpty()) {
            throw new ApiDataNotExistException();
        }
        return forecasts;
    }

    /**
     * api로 데이터를 조회할 수 없을 때 리턴하는 Exception
     */
    @ExceptionHandler(ApiDataNotExistException.class)
    public ResponseEntity<String> apiDataNotExistException() {
        return ResponseEntity
                .status(ApiDataNotExistException.errorCode)
                .body(ApiDataNotExistException.message);
    }
}
