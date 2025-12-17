package org.example.nova.sitesafety.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "현장 목록 조회 응답 DTO")
public class SiteListResDTO {

    @Schema(description = "현장 목록")
    private List<SiteInfoVO> results;

    @Schema(description = "페이징 정보")
    private PaginationDTO pagination;
}