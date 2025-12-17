package org.example.nova.sitesafety.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "페이징 정보")
public class PaginationDTO {

    @Schema(description = "현재 페이지 번호", example = "1")
    private int pageNum;

    @Schema(description = "페이지당 데이터 수", example = "10")
    private int pageSize;

    @Schema(description = "전체 데이터 수", example = "57")
    private int totalCnt;

    @Schema(description = "전체 페이지 수", example = "6")
    private int totalPageCnt;
}
