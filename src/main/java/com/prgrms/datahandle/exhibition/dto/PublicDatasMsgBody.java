package com.prgrms.datahandle.exhibition.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "msgBody")
public class PublicDatasMsgBody {
  private int totalCount;
  private int cPage;
  private int rows;
  private String realmCode;
  private String from;
  private String to;
  private int sortStdr;
  private List<PublicDataPerfor> perforList;

  public PublicDatasMsgBody(int totalCount, int cPage, int rows, String realmCode,
      String from, String to, int sortStdr,
      List<PublicDataPerfor> perforList) {
    this.totalCount = totalCount;
    this.cPage = cPage;
    this.rows = rows;
    this.realmCode = realmCode;
    this.from = from;
    this.to = to;
    this.sortStdr = sortStdr;
    this.perforList = perforList;
  }
}
