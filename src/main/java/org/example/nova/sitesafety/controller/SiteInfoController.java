package org.example.nova.sitesafety.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.nova.sitesafety.model.SiteCreateReqDTO;
import org.example.nova.sitesafety.model.SiteDetailResDTO;
import org.example.nova.sitesafety.service.SiteInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sitesafety/v1")
@RequiredArgsConstructor
@Tag(name = "SiteSafety - SiteInfo API", description = "현장 정보 관리 API")
public class SiteInfoController {

    private final SiteInfoService siteInfoService;

    @Operation(summary = "SS090101 현장 등록")
    @PostMapping
    public ResponseEntity<Void> createSiteInfo(
            @RequestBody @Valid SiteCreateReqDTO req
    ) {
        siteInfoService.createSiteInfo(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "SS090102 현장 상세 조회")
    @GetMapping("/{siteCode}")
    public ResponseEntity<SiteDetailResDTO> getSiteInfo(
            @PathVariable String siteCode
    ) {
        SiteDetailResDTO result = siteInfoService.getSiteInfo(siteCode);
        return ResponseEntity.ok(result);
    }

}
