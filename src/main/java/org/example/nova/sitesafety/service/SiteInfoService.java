package org.example.nova.sitesafety.service;

import lombok.RequiredArgsConstructor;
import org.example.nova.sitesafety.mapper.SiteInfoMapper;
import org.example.nova.sitesafety.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SiteInfoService {

    private final SiteInfoMapper siteInfoMapper;

    /**
     * 현장 목록 조회
     * - 페이징 계산
     * - 전체 건수 조회
     */
    @Transactional(readOnly = true)
    public SiteListResDTO getSiteInfoList(SiteListReqDTO req) {

        // 실제 데이터 목록 조회
        List<SiteInfoVO> list =
                siteInfoMapper.selectSiteInfoList(req);

        // 전체 데이터 개수 조회
        int totalCnt =
                siteInfoMapper.countSiteInfo(req);

        // 전체 페이지 수 계산
        int totalPageCnt =
                totalCnt % req.getPageSize() == 0
                        ? totalCnt / req.getPageSize()
                        : (totalCnt / req.getPageSize()) + 1;

        // 응답 DTO 구성
        return SiteListResDTO.builder()
                .results(list)
                .pagination(
                        PaginationDTO.builder()
                                .pageNum(req.getPageNum())
                                .pageSize(req.getPageSize())
                                .totalCnt(totalCnt)
                                .totalPageCnt(totalPageCnt)
                                .build()
                )
                .build();
    }

    /**
     * 현장 등록
     * - 지역 + 도시 + 일련번호로 siteCode 생성
     */
    public void createSiteInfo(SiteCreateReqDTO req) {

        // 해당 지역/도시의 마지막 일련번호 조회
        int lastSerialNum =
                siteInfoMapper.selectLastSerialNum(req);

        int newSerialNum = lastSerialNum + 1;

        SerialNumUpdateDTO updateDTO =
                SerialNumUpdateDTO.builder()
                        .regionCode(req.getRegionCode())
                        .cityCode(req.getCityCode())
                        .lastSerialNum(newSerialNum)
                        .build();

        siteInfoMapper.updateLastSerialNum(updateDTO);

        // siteCode 생성 규칙
        // 예: ss + 지역코드 + 도시코드 + 0 + 일련번호
        String siteCode =
                "ss" + req.getRegionCode()
                        + req.getCityCode()
                        + "0"
                        + newSerialNum;

        // 생성된 값 세팅
        req.setSiteCode(siteCode);
        req.setSerialNum(newSerialNum);

        // DB insert
        siteInfoMapper.insertSiteInfo(req);
    }

    /**
     * 현장 상세 조회
     */
    @Transactional(readOnly = true)
    public SiteDetailResDTO getSiteInfo(String siteCode) {

        SiteDetailResDTO result =
                siteInfoMapper.selectSiteInfoBySiteCode(siteCode);

        // 조회 결과가 없을 경우 예외
        if (result == null) {
            throw new IllegalArgumentException("현장 정보가 존재하지 않습니다.");
        }

        return result;
    }
}
