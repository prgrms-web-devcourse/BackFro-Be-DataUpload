package com.prgrms.datahandle.exhibition.dto;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "response")
public class PublicDataResponse {
  private Map<String, String> comMsgHeader;
  private PublicDataMsgBody msgBody;

  public PublicDataResponse(Map<String, String> comMsgHeader,
      PublicDataMsgBody msgBody) {
    this.comMsgHeader = comMsgHeader;
    this.msgBody = msgBody;
  }
}
