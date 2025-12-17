package org.example.nova.sitesafety.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "현장 상세 조회 응답 DTO")
public class SiteDetailResDTO {

    @Schema(description = "현장 코드", example = "SS010203")
    private String siteCode;

    @Schema(description = "현장명", example = "광명센트럴아이파크")
    private String siteName;

    @Schema(description = "주소")
    private String address;

    @Schema(description = "위도")
    private String latitude;

    @Schema(description = "경도")
    private String longitude;

    @Schema(description = "생성 일시")
    private LocalDateTime createdAt;
}
