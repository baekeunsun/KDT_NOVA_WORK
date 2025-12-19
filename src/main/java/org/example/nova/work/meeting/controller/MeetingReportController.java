package org.example.nova.work.meeting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.nova.work.meeting.model.MeetingInfoRes;
import org.example.nova.work.meeting.model.MeetingNameResDTO;
import org.example.nova.work.meeting.service.MeetingReportService;
import org.example.nova.work.meeting.service.MeetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/work/meeting")
@RequiredArgsConstructor
@Tag(name = "Meeting - MeetingReport API", description = "회의 리포트 API")
public class MeetingReportController {

    private final MeetingReportService meetingReportService;
    private final MeetingService meetingService;

    @Operation(summary = "WK010101 회의 이름 조회 (예시 API)")
    @GetMapping("/{meetingId}/name")
    public ResponseEntity<MeetingNameResDTO> getMeetingName(
            @PathVariable int meetingId
    ) {
        MeetingNameResDTO response = meetingReportService.getMeetingName(meetingId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "1번 : 회의 기본 정보 조회")
    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingInfoRes> getMeetingInfo(@PathVariable int meetingId) {
        return ResponseEntity.ok(meetingService.getMeetingInfo(meetingId));
    }
}
