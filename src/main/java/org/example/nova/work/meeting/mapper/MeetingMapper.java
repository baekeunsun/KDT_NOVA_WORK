package org.example.nova.work.meeting.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nova.work.meeting.model.MeetingBase;
import org.example.nova.work.meeting.model.UtteranceInfo;

import java.util.List;

@Mapper
public interface MeetingMapper {

    // 회의 이름 조회
    String selectMeetingName(@Param("meetingId") int meetingId);

    // 회의 기본 정보 조회
    MeetingBase findById(@Param("meetingId") int meetingId);

    // 공유받은 사용자 수 조회 (COUNT 쿼리 허용)
    int countViewers(@Param("meetingId") int meetingId);

    // 회의 발화 데이터 조회
    List<UtteranceInfo> findUtterancesByMeetingId(@Param("meetingId") int meetingId);
}