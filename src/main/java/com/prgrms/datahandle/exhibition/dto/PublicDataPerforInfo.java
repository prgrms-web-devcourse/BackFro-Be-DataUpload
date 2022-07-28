package com.prgrms.datahandle.exhibition.dto;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "perforInfo")
public class PublicDataPerforInfo {
  private String seq;
  private String title;
  private String startDate;
  private String endDate;
  private String place;
  private String realmName;
  private String area;
  private String subTitle;
  private String price;
  private String contents1;
  private String contents2;
  private String url;
  private String phone;
  private String imgUrl;
  private Double gpsX;
  private Double gpsY;
  private String placeUrl;
  private String placeAddr;
  private String placeSeq;

  public PublicDataPerforInfo(String seq, String title, String startDate, String endDate,
      String place, String realmName, String area, String subTitle, String price,
      String contents1, String contents2, String url, String phone, String imgUrl,
      Double gpsX, Double gpsY, String placeUrl, String placeAddr, String placeSeq) {
    this.seq = seq;
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
    this.place = place;
    this.realmName = realmName;
    this.area = area;
    this.subTitle = subTitle;
    this.price = price;
    this.contents1 = contents1;
    this.contents2 = contents2;
    this.url = url;
    this.phone = phone;
    this.imgUrl = imgUrl;
    this.gpsX = gpsX;
    this.gpsY = gpsY;
    this.placeUrl = placeUrl;
    this.placeAddr = placeAddr;
    this.placeSeq = placeSeq;
  }
}
