package com.prgrms.datahandle.exhibition.dto;

import com.prgrms.datahandle.exhibition.domain.Exhibition;
import java.util.List;
import lombok.Getter;

@Getter
public class ExhibitionResponse {
  private List<Exhibition> exhibitions;
  private int totalSize;

  public ExhibitionResponse(List<Exhibition> exhibitions, int totalSize) {
    this.exhibitions = exhibitions;
    this.totalSize = totalSize;
  }
}
