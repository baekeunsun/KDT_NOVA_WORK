package org.example.nova.work.meeting.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MeetingMapper {

    // 회의 이름 조회
    String selectMeetingName(@Param("meetingId") int meetingId);
}