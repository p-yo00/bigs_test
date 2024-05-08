## API 명세
**GET /forecast**

: DB에서 현재 날짜, 시간에 기록된 [송산1동]의 날씨 예보를 가져옵니다.

**POST /forecast**

: 단기예보 API에서 현재 날짜, 시간의 날씨 예보를 가져와서 DB(Forecast)에 저장합니다.


## 모듈 구조
**forecast**: root 모듈, lombok 사용

**--apicall**: open api를 호출하는 서비스 모듈

**--application**: spring boot application을 실행하는 모듈

**--domain**: 날씨 예보 api 관련 서비스 모듈 (entity, repository 포함)

**--exception**: 데이터가 없을 때 리턴하는 ApiDataNotExistException (204)
