package org.example.nova.sitesafety.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "현장 정보 VO (DB 조회 결과)")
public class SiteInfoVO {

    @Schema(description = "현장 코드", example = "ss010201")
    private String siteCode;

    @Schema(description = "현장명", example = "광명센트럴 아이파크")
    private String siteName;

    @Schema(description = "지역 코드", example = "01")
    private String regionCode;

    @Schema(description = "도시 코드", example = "02")
    private String cityCode;

    @Schema(description = "생성 일시", example = "2025-01-01T12:00:00")
    private LocalDateTime createdAt;
}