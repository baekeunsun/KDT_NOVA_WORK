package org.example.nova.work.meeting.service;

import lombok.RequiredArgsConstructor;
import org.example.nova.work.meeting.mapper.MeetingMapper;
import org.example.nova.work.meeting.model.MeetingNameResDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingReportService {

    private final MeetingMapper meetingMapper;

    public MeetingNameResDTO getMeetingName(int meetingId) {
        String meetingName = meetingMapper.selectMeetingName(meetingId);

        if (meetingName == null) {
            throw new IllegalArgumentException("존재하지 않는 회의입니다.");
        }

        return new MeetingNameResDTO(meetingId, meetingName);
    }
}
