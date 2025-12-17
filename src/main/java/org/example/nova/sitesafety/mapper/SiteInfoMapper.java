package org.example.nova.sitesafety.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.nova.sitesafety.model.*;

import java.util.List;

@Mapper
public interface SiteInfoMapper {

    /**
     * 현장 목록 조회
     */
    List<SiteInfoVO> selectSiteInfoList(SiteListReqDTO req);

    /**
     * 전체 현장 수 조회 (페이징용)
     */
    int countSiteInfo(SiteListReqDTO req);

    /**
     * 지역/도시 기준 마지막 일련번호 조회
     */
    int selectLastSerialNum(SiteCreateReqDTO req);

    /**
     * 현장 정보 저장
     */
    void insertSiteInfo(SiteCreateReqDTO req);

    /**
     * siteCode로 현장 상세 조회
     */
    SiteDetailResDTO selectSiteInfoBySiteCode(String siteCode);

    /**
     * 지역/도시별 마지막 시리얼 번호 업데이트
     */
    int updateLastSerialNum(SerialNumUpdateDTO dto);
}