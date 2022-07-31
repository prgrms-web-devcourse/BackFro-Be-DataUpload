# 공공 데이터 DB 업로드

## 설명
전시회 공공 데이터를 DB에 업로딩하기 위한 어플리케이션입니다.

### /api/v1/public-datas/init
DB가 비어있는 경우 올해 전시회 정보를 DB에 저장합니다.

### /api/v1/public-datas/update
(현재 ~ 현재+3개월) 동안 업데이트된 전시회 정보를 DB에 추가합니다.