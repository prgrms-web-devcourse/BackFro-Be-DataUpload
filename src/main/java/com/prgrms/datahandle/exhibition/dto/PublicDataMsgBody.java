package com.prgrms.datahandle.exhibition.dto;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "msgBody")
public class PublicDataMsgBody {
  private String seq;
  private PublicDataPerforInfo perforInfo;

  public PublicDataMsgBody(String seq,
      PublicDataPerforInfo perforInfo) {
    this.seq = seq;
    this.perforInfo = perforInfo;
  }
}
