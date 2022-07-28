package com.prgrms.datahandle.exhibition.controller;

import com.prgrms.datahandle.exhibition.service.DataUploadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public-datas")
@RequiredArgsConstructor
public class DataUploadingController {
  private final DataUploadingService dataUploadingService;

  @PostMapping("/init")
  public ResponseEntity<String> initExhibitionData() {
    dataUploadingService.initData();

    return ResponseEntity
        .ok()
        .body("init success");
  }
}
