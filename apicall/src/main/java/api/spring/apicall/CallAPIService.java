package api.spring.apicall;

import api.spring.exception.ApiDataNotExistException;
import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CallAPIService {
    @Value("${VilageFcstInfoService.api-key}")
    private String apiKey;

    public List<Map<String, Object>> callApi(String lat, String lnt,
                                             LocalDateTime dateTime) {
        List<Map<String, Object>> resultMapList;
        try {
            Map<String, String> param = new HashMap<>(); // api 호출 파라미터 map
            param.put("ServiceKey", apiKey);
            param.put("dataType", "JSON");
            param.put("pageNo", "1");
            param.put("numOfRows", "15"); // 최대 카테고리 수를 고려해서 고정
            param.put("base_date", dateTime.format(DateTimeFormatter.ofPattern(
                    "yyyyMMdd")));
            param.put("base_time", dateTime.format(DateTimeFormatter.ofPattern(
                    "HHmm")));
            param.put("nx", lat);
            param.put("ny", lnt);

            String url = "https://apis.data.go" +
                    ".kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
            String[] paths = "response.body.items".split("\\.");

            JSONObject responseJson = callGetApiReturnJson(url, param, paths);

            String itemArray = responseJson != null ?
                    responseJson.getJSONArray("item").toString() : null;
            Gson gson = new Gson();
            resultMapList = gson.fromJson(itemArray, List.class);
        } catch (Exception e) {
            throw new ApiDataNotExistException();
        }

        for (Map<String, Object> map : resultMapList) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getValue() + " " + entry.getValue().getClass());
            }
        }

        return resultMapList;
    }

    /**
     * Get api를 호출하고 결과값 Json을 가져와 리턴한다.
     */
    private JSONObject callGetApiReturnJson(String url,
                                            Map<String, String> param,
                                            String[] paths) {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        HttpClient httpClient = HttpClientBuilder.create().build();
        factory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());

        // url에 파라미터 정보를 더한다.
        StringBuilder urlSb = new StringBuilder(url);
        urlSb.append("?");
        for (Map.Entry<String, String> entry : param.entrySet()) {
            urlSb.append(entry.getKey());
            urlSb.append("=");
            urlSb.append(entry.getValue());
            urlSb.append("&");
        }
        if (!param.isEmpty()) { // 마지막 & 제거
            urlSb.deleteCharAt(urlSb.length() - 1);
        }

        UriComponents uri =
                UriComponentsBuilder.fromHttpUrl(urlSb.toString()).build();
        String respJsonStr =
                restTemplate.exchange(uri.toString(), HttpMethod.GET,
                        entity, String.class).getBody();

        JSONObject responseJson = new JSONObject(respJsonStr);
        for (String path : paths) {
            if (responseJson != null && responseJson.has(path)) {
                responseJson = responseJson.getJSONObject(path);
            } else {
                throw new ApiDataNotExistException();
            }
        }
        return responseJson;
    }
}
