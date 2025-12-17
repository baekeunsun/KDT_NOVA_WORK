package org.example.nova.sitesafety.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "현장 등록 요청 DTO")
public class SiteCreateReqDTO {

    @Schema(description = "현장명", example = "광명센트럴 아이파크")
    private String siteName;

    @Schema(description = "지역 코드", example = "01")
    private String regionCode;

    @Schema(description = "도시 코드", example = "02")
    private String cityCode;

    @Schema(description = "주소")
    private String address;

    @Schema(description = "위도")
    private String latitude;

    @Schema(description = "경도")
    private String longitude;

    @Schema(description = "현장 코드 (서버에서 자동 생성)", example = "ss010201", accessMode = Schema.AccessMode.READ_ONLY)
    private String siteCode;

    @Schema(description = "일련번호 (서버에서 자동 계산)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int serialNum;
}