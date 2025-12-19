package org.example.nova.work.meeting.service;

import lombok.RequiredArgsConstructor;
import org.example.nova.work.meeting.mapper.MeetingMapper;
import org.example.nova.work.meeting.model.MeetingNameResDTO;
import org.example.nova.work.meeting.model.SpeakerStat;
import org.example.nova.work.meeting.model.UtteranceInfo;
import org.example.nova.work.meeting.util.SpeakerStatComparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public List<SpeakerStat> getTopSpeakers(int meetingId, int limit) {

        // 1. 발화 데이터 모두 조회
        List<UtteranceInfo> utterances =
                meetingMapper.findUtterancesByMeetingId(meetingId);

        // 2. 발화자별 통계 집계
        Map<String, SpeakerStat> speakerMap = new HashMap<>();

        for (UtteranceInfo utterance : utterances) {
            String speakerName = utterance.getSpeakerName();

            // Map에 없으면 새로 생성
            if (!speakerMap.containsKey(speakerName)) {
                speakerMap.put(speakerName, new SpeakerStat(speakerName));
            }

            SpeakerStat stat = speakerMap.get(speakerName);

            // 발화 건수 증가
            stat.addCount();
        }

        // 3. Map -> List 변환
        List<SpeakerStat> speakerList =
                new ArrayList<>(speakerMap.values());

        // 4단계: 정렬 (복합 기준)
        Collections.sort(
                speakerList,
                new SpeakerStatComparator()
        );

        // 5. 상위 limit 개만 반환
        if (speakerList.size() > limit) {
            return speakerList.subList(0, limit);
        }

        return speakerList;
    }
}
