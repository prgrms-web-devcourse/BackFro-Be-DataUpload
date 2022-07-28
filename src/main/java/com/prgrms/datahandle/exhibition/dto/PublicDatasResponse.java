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
public class PublicDatasResponse {
  private Map<String, String> comMsgHeader;
  private PublicDatasMsgBody msgBody;

  public PublicDatasResponse(Map<String, String> comMsgHeader,
      PublicDatasMsgBody msgBody) {
    this.comMsgHeader = comMsgHeader;
    this.msgBody = msgBody;
  }
}
