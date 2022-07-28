package com.prgrms.datahandle.exhibition.dto;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "perforList")
public class PublicDataPerfor {
  private String seq;
  private String title;
  private String startDate;
  private String endDate;
  private String place;
  private String realmName;
  private String area;
  private String thumbnail;
  private String gpsX;
  private String gpsY;

  public PublicDataPerfor(String seq, String title, String startDate, String endDate,
      String place, String realmName, String area, String thumbnail, String gpsX,
      String gpsY) {
    this.seq = seq;
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
    this.place = place;
    this.realmName = realmName;
    this.area = area;
    this.thumbnail = thumbnail;
    this.gpsX = gpsX;
    this.gpsY = gpsY;
  }
}
