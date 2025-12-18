package org.example.nova.work.meeting.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MeetingMapper {

    String selectMeetingName(@Param("meetingId") int meetingId);
}