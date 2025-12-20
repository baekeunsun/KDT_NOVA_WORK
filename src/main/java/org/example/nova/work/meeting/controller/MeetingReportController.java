package org.example.nova.work.meeting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.nova.work.meeting.model.MeetingNameResDTO;
import org.example.nova.work.meeting.service.MeetingReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/work/meeting")
@RequiredArgsConstructor
@Tag(name = "Meeting - MeetingReport API", description = "회의 리포트 API")
public class MeetingReportController {

    private final MeetingReportService meetingReportService;

    @Operation(summary = "WK010101 회의 이름 조회 (예시 API)")
    @GetMapping("/{meetingId}/name")
    public ResponseEntity<MeetingNameResDTO> getMeetingName(
            @PathVariable int meetingId
    ) {
        MeetingNameResDTO response = meetingReportService.getMeetingName(meetingId);
        return ResponseEntity.ok(response);
    }

}
