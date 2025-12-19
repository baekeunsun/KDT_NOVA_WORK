package org.example.nova.work.meeting.service;

import lombok.RequiredArgsConstructor;
import org.example.nova.work.meeting.mapper.MeetingMapper;
import org.example.nova.work.meeting.model.MeetingBase;
import org.example.nova.work.meeting.model.MeetingInfoRes;
import org.example.nova.work.meeting.model.MeetingNameResDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingService {

    private final MeetingMapper meetingMapper;


    public MeetingInfoRes getMeetingInfo(int meetingId) {

        // 1. Mapper를 사용해서 회의 정보 조회
        MeetingBase meeting = meetingMapper.findById(meetingId);

        // 2. 회의가 없으면 예외 발생 (if문 사용)
        if (meeting == null) {
            throw new IllegalArgumentException("존재하지 않는 회의입니다.");
        }

        // 3. Mapper를 사용해서 공유받은 사용자 수 조회
        int viewerCount = meetingMapper.countViewers(meetingId);

        // 4. MeetingInfoRes 객체를 만들고 데이터 채우기
        MeetingInfoRes res = new MeetingInfoRes();
        res.setMeetingId(meeting.getId());
        res.setName(meeting.getName());
        res.setAttendeeList(meeting.getAttendeeList());
        res.setViewerCount(viewerCount);
        res.setCreatedAt(meeting.getCrtDtime());
        res.setEndedAt(meeting.getEndDtime());

        // 5. 결과 반환
        return res;
    }
}
