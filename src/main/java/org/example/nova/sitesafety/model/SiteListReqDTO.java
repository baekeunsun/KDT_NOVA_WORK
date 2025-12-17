package org.example.nova.sitesafety.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "현장 목록 조회 요청 DTO")
public class SiteListReqDTO {

    @Schema(description = "페이지 번호 (1부터 시작)", example = "1")
    private int pageNum = 1;

    @Schema(description = "페이지당 조회 건수", example = "10")
    private int pageSize = 10;

    @Schema(description = "지역 코드", example = "01")
    private String regionCode;

    @Schema(description = "도시 코드", example = "02")
    private String cityCode;

    /**
     * MyBatis OFFSET 계산용
     */
    @Schema(hidden = true) // Swagger 화면에 노출 안 함
    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
