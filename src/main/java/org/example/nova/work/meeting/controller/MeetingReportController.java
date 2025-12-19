package org.example.nova.work.meeting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.nova.work.meeting.model.KeywordStat;
import org.example.nova.work.meeting.model.MeetingInfoRes;
import org.example.nova.work.meeting.model.MeetingNameResDTO;
import org.example.nova.work.meeting.model.SpeakerStat;
import org.example.nova.work.meeting.service.MeetingReportService;
import org.example.nova.work.meeting.service.MeetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "2번 : 회의 발화자 Top-K 조회")
    @GetMapping("/{meetingId}/speakers")
    public ResponseEntity<List<SpeakerStat>> getTopSpeakers(
            @PathVariable int meetingId,
            @RequestParam(defaultValue = "3") int limit
    ) {
        return ResponseEntity.ok(meetingReportService.getTopSpeakers(meetingId, limit));
    }

    @Operation(summary = "3번 : 회의 키워드 Top-K 조회")
    @GetMapping("/{meetingId}/keywords")
    public ResponseEntity<List<KeywordStat>> getTopKeywords(
            @PathVariable int meetingId,
            @RequestParam(defaultValue = "5") int limit) {

        return ResponseEntity.ok(meetingReportService.getTopKeywords(meetingId, limit));
    }
}
