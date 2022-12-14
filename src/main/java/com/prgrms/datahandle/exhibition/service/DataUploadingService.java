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
    put("???????????????", SEOUL);
    put("?????????",  SEOUL);
    put("??????",  SEOUL);
    put("???????????????", BUSAN);
    put("?????????", BUSAN);
    put("??????", BUSAN);
    put("???????????????", DAEGU);
    put("?????????", DAEGU);
    put("??????", DAEGU);
    put("???????????????", INCHEON);
    put("?????????", INCHEON);
    put("??????", INCHEON);
    put("???????????????", DAEJEON);
    put("?????????", DAEJEON);
    put("??????", DAEJEON);
    put("???????????????", GWANGJU);
    put("?????????", GWANGJU);
    put("??????", GWANGJU);
    put("???????????????", ULSAN);
    put("?????????", ULSAN);
    put("??????", ULSAN);
    put("?????????????????????", SEJONG);
    put("?????????", SEJONG);
    put("??????", SEJONG);
    put("?????????", GYEONGGI);
    put("??????", GYEONGGI);
    put("?????????", GANGWON);
    put("??????",  GANGWON);
    put("????????????", CHUNGBUK);
    put("??????", CHUNGBUK);
    put("????????????", CHUNGNAM);
    put("??????", CHUNGNAM);
    put("????????????", JEONBUK);
    put("??????", JEONBUK);
    put("????????????", JEONNAM);
    put("??????", JEONNAM);
    put("????????????", GYEONGBUK);
    put("??????", GYEONGBUK);
    put("????????????", GYEONGNAM);
    put("??????", GYEONGNAM);

    put("?????????????????????", JEJU);
    put("?????????", JEJU);
    put("??????", JEJU);
  }};
  private final RestTemplate restTemplate = new RestTemplate();

  private final ExhibitionRepository exhibitionRepository;

  @Value("${key.serviceKey}")
  private String serviceKey;

  /**
   * ?????? ???????????? ????????????.
   */
  @Transactional
  public void initData() {
    checkAlreadyInitialized();

    int year = LocalDate.now().getYear();
    for(PublicDataPerfor publicDataPerfor: getPerforList(year)) {
      PublicDataPerforInfo perforInfo = getPerforInfo(publicDataPerfor.getSeq());

      // ?????? ?????? ??????, ????????? ?????? ??????, ?????? ?????? ??????(startDate, endDate), ?????? ?????? ??????, ?????? ?????? ??????, ?????? ?????? ?????? ??????, ?????? ?????? ??????(gpsX(??????), gpsY(??????)), ????????? ?????? ??????, ???????????? ?????? ????????? ??????
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
