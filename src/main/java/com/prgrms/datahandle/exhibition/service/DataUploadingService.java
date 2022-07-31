package com.prgrms.datahandle.exhibition.service;

import static com.prgrms.datahandle.exhibition.domain.Area.BUSAN;
import static com.prgrms.datahandle.exhibition.domain.Area.CHUNGBUK;
import static com.prgrms.datahandle.exhibition.domain.Area.CHUNGNAM;
import static com.prgrms.datahandle.exhibition.domain.Area.DAEGU;
import static com.prgrms.datahandle.exhibition.domain.Area.DAEJEON;
import static com.prgrms.datahandle.exhibition.domain.Area.GANGWON;
import static com.prgrms.datahandle.exhibition.domain.Area.GWANGJU;
import static com.prgrms.datahandle.exhibition.domain.Area.GYEONGBUK;
import static com.prgrms.datahandle.exhibition.domain.Area.GYEONGGI;
import static com.prgrms.datahandle.exhibition.domain.Area.GYEONGNAM;
import static com.prgrms.datahandle.exhibition.domain.Area.INCHEON;
import static com.prgrms.datahandle.exhibition.domain.Area.JEJU;
import static com.prgrms.datahandle.exhibition.domain.Area.JEONBUK;
import static com.prgrms.datahandle.exhibition.domain.Area.JEONNAM;
import static com.prgrms.datahandle.exhibition.domain.Area.SEJONG;
import static com.prgrms.datahandle.exhibition.domain.Area.SEOUL;
import static com.prgrms.datahandle.exhibition.domain.Area.ULSAN;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasText;

import com.prgrms.datahandle.exhibition.domain.Area;
import com.prgrms.datahandle.exhibition.domain.Exhibition;
import com.prgrms.datahandle.exhibition.domain.Location;
import com.prgrms.datahandle.exhibition.domain.Period;
import com.prgrms.datahandle.exhibition.domain.repository.ExhibitionRepository;
import com.prgrms.datahandle.exhibition.dto.PublicDataPerfor;
import com.prgrms.datahandle.exhibition.dto.PublicDataPerforInfo;
import com.prgrms.datahandle.exhibition.dto.PublicDataResponse;
import com.prgrms.datahandle.exhibition.dto.PublicDatasResponse;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataUploadingService {
  private final Map<String, Area> areaMap = new HashMap<>(){{
    put("서울특별시", SEOUL);
    put("서울시",  SEOUL);
    put("서울",  SEOUL);
    put("부산광역시", BUSAN);
    put("부산시", BUSAN);
    put("부산", BUSAN);
    put("대구광역시", DAEGU);
    put("대구시", DAEGU);
    put("대구", DAEGU);
    put("인천광역시", INCHEON);
    put("인천시", INCHEON);
    put("인천", INCHEON);
    put("대전광역시", DAEJEON);
    put("대전시", DAEJEON);
    put("대전", DAEJEON);
    put("광주광역시", GWANGJU);
    put("광주시", GWANGJU);
    put("광주", GWANGJU);
    put("울산광역시", ULSAN);
    put("울산시", ULSAN);
    put("울산", ULSAN);
    put("세종특별자치시", SEJONG);
    put("세종시", SEJONG);
    put("세종", SEJONG);
    put("경기도", GYEONGGI);
    put("경기", GYEONGGI);
    put("강원도", GANGWON);
    put("강원",  GANGWON);
    put("충청북도", CHUNGBUK);
    put("충북", CHUNGBUK);
    put("충청남도", CHUNGNAM);
    put("충남", CHUNGNAM);
    put("전라북도", JEONBUK);
    put("전북", JEONBUK);
    put("전라남도", JEONNAM);
    put("전남", JEONNAM);
    put("경상북도", GYEONGBUK);
    put("경북", GYEONGBUK);
    put("경상남도", GYEONGNAM);
    put("경남", GYEONGNAM);

    put("제주특별자치도", JEJU);
    put("제주도", JEJU);
    put("제주", JEJU);
  }};
  private final RestTemplate restTemplate = new RestTemplate();

  private final ExhibitionRepository exhibitionRepository;

  @Value("${key.serviceKey}")
  private String serviceKey;

  /**
   * 올해 데이터를 저장한다.
   */
  @Transactional
  public void initData() {
    checkAlreadyInitialized();

    int year = LocalDate.now().getYear();
    for(PublicDataPerfor publicDataPerfor: getPerforList(year)) {
      PublicDataPerforInfo perforInfo = getPerforInfo(publicDataPerfor.getSeq());

      // 제목 없는 경우, 이미지 없는 경우, 날짜 없는 경우(startDate, endDate), 가격 없는 경우, 장소 없는 경우, 상세 주소 없는 경우, 좌표 없는 경우(gpsX(경도), gpsY(위도)), 연락처 없는 경우, 지원하지 않는 지역인 경우
      if(!hasText(perforInfo.getTitle()) || !hasText(perforInfo.getImgUrl()) || !hasText(perforInfo.getStartDate()) || !hasText(perforInfo.getEndDate()) ||
          !hasText(perforInfo.getPrice()) || !hasText(perforInfo.getPlace()) || !hasText(perforInfo.getPlaceAddr()) || isNull(perforInfo.getGpsX()) || isNull(perforInfo.getGpsY()) ||
          !hasText(perforInfo.getPhone()) || isNull(areaMap.get(perforInfo.getPlaceAddr().split(" ")[0].trim()))) {
        continue;
      }

      Period period = getPeriod(perforInfo);
      Location location = getLocation(perforInfo);

      exhibitionRepository.save(Exhibition.builder()
          .seq(Integer.parseInt(perforInfo.getSeq()))
          .name(perforInfo.getTitle())
          .period(period)
          .location(location)
          .inquiry(perforInfo.getPhone())
          .fee(perforInfo.getPrice())
          .thumbnail(perforInfo.getImgUrl())
          .url(perforInfo.getUrl())
          .placeUrl(perforInfo.getUrl())
          .build());
    }
  }

  @Transactional
  public int updateData() {
    int count = 0;
    for(PublicDataPerfor publicDataPerfor: getPerforListForUpdate()) {
      String seq = publicDataPerfor.getSeq();

      if(exhibitionRepository.findBySeq(Integer.parseInt(seq)).isPresent()) {
        continue;
      }

      PublicDataPerforInfo perforInfo = getPerforInfo(seq);

      if(!hasText(perforInfo.getTitle()) || !hasText(perforInfo.getImgUrl()) || !hasText(perforInfo.getStartDate()) || !hasText(perforInfo.getEndDate()) ||
          !hasText(perforInfo.getPrice()) || !hasText(perforInfo.getPlace()) || !hasText(perforInfo.getPlaceAddr()) || isNull(perforInfo.getGpsX()) || isNull(perforInfo.getGpsY()) ||
          !hasText(perforInfo.getPhone()) || isNull(areaMap.get(perforInfo.getPlaceAddr().split(" ")[0].trim()))) {
        continue;
      }

      Period period = getPeriod(perforInfo);
      Location location = getLocation(perforInfo);

      exhibitionRepository.save(Exhibition.builder()
          .seq(Integer.parseInt(perforInfo.getSeq()))
          .name(perforInfo.getTitle())
          .period(period)
          .location(location)
          .inquiry(perforInfo.getPhone())
          .fee(perforInfo.getPrice())
          .thumbnail(perforInfo.getImgUrl())
          .url(perforInfo.getUrl())
          .placeUrl(perforInfo.getUrl())
          .build());

      count++;
    }

    return count;
  }

  private void checkAlreadyInitialized() {
    long cntOfExhibitions = exhibitionRepository.count();
    if(cntOfExhibitions != 0) {
      throw new RuntimeException("already initialized!");
    }
  }

  private List<PublicDataPerfor> getPerforList(int year) {
    String listUrl = "http://www.culture.go.kr/openapi/rest/publicperformancedisplays/realm";

    try {
      URI listRequestURI = new URI(listUrl+"?serviceKey="+serviceKey+"&sortStdr=1&realmCode=D000&cPage=1&rows=1000&from="+year+"0101&to="+year+"1231");
      PublicDatasResponse publicDatasResponse = restTemplate.getForObject(listRequestURI, PublicDatasResponse.class);
      return publicDatasResponse.getMsgBody().getPerforList();
    }catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("get public data fail!");
    }
  }

  private List<PublicDataPerfor> getPerforListForUpdate() {
    String listUrl = "http://www.culture.go.kr/openapi/rest/publicperformancedisplays/realm";
    String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String endDate = LocalDate.now().plusMonths(3).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    try {
      URI listRequestURI = new URI(listUrl+"?serviceKey="+serviceKey+"&sortStdr=1&realmCode=D000&cPage=1&rows=1000&from="+startDate+"&to="+endDate);
      PublicDatasResponse publicDatasResponse = restTemplate.getForObject(listRequestURI, PublicDatasResponse.class);
      return publicDatasResponse.getMsgBody().getPerforList();
    }catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("get public data fail!");
    }
  }


  private PublicDataPerforInfo getPerforInfo(String seq) {
    String detailUrl = "http://www.culture.go.kr/openapi/rest/publicperformancedisplays/d/";

    try {
      URI detailRequestUri = new URI(detailUrl + "?serviceKey=" + serviceKey + "&seq=" + seq);
      PublicDataResponse publicDataResponse = restTemplate.getForObject(detailRequestUri, PublicDataResponse.class);
      return publicDataResponse.getMsgBody().getPerforInfo();
    }catch(Exception e){
      e.printStackTrace();
      throw new RuntimeException("get public data fail!");
    }
  }

  private Period getPeriod(PublicDataPerforInfo perforInfo) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate startDate = LocalDate.parse(perforInfo.getStartDate(), formatter);
    LocalDate endDate = LocalDate.parse(perforInfo.getEndDate(), formatter);
    return new Period(startDate, endDate);
  }

  private Location getLocation(PublicDataPerforInfo perforInfo) {
    Double latitude = Double.parseDouble(String.format("%.6f", perforInfo.getGpsY()));
    Double longitude = Double.parseDouble(String.format("%.6f", perforInfo.getGpsX()));
    Area area = areaMap.get(perforInfo.getPlaceAddr().split(" ")[0].trim());

    return Location.builder()
        .latitude(latitude)
        .longitude(longitude)
        .area(area)
        .place(perforInfo.getPlace())
        .address(perforInfo.getPlaceAddr())
        .build();
  }
}
