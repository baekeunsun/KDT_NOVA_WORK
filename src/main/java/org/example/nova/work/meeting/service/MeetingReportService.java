package org.example.nova.work.meeting.service;

import lombok.RequiredArgsConstructor;
import org.example.nova.work.meeting.mapper.MeetingMapper;
import org.example.nova.work.meeting.model.KeywordStat;
import org.example.nova.work.meeting.model.MeetingNameResDTO;
import org.example.nova.work.meeting.model.SpeakerStat;
import org.example.nova.work.meeting.model.UtteranceInfo;
import org.example.nova.work.meeting.util.KeywordEntryComparator;
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

        // 1. 발화 데이터 조회
        List<UtteranceInfo> utterances =
                meetingMapper.findUtterancesByMeetingId(meetingId);

        // 2. 발화자별 집계
        Map<String, SpeakerStat> speakerMap = new HashMap<>();

        for (UtteranceInfo utterance : utterances) {
            String speakerName = utterance.getSpeakerName();

            if (!speakerMap.containsKey(speakerName)) {
                speakerMap.put(speakerName, new SpeakerStat(speakerName));
            }

            speakerMap.get(speakerName).addCount();
        }

        // 3. PriorityQueue 생성 (⭐ 핵심)
        PriorityQueue<SpeakerStat> pq =
                new PriorityQueue<>(new SpeakerStatComparator());

        // 4. Top-K 유지
        for (SpeakerStat stat : speakerMap.values()) {
            pq.offer(stat);

            // K개 초과하면 가장 약한 놈 제거
            if (pq.size() > limit) {
                pq.poll();
            }
        }

        // 5. 결과 꺼내기
        List<SpeakerStat> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        return result;
    }


    public List<KeywordStat> getTopKeywords(int meetingId, int limit) {

        // 1. 발화 데이터 조회
        List<UtteranceInfo> utterances =
                meetingMapper.findUtterancesByMeetingId(meetingId);

        // 2. 발화 텍스트 합치기
        StringBuilder sb = new StringBuilder();
        for (UtteranceInfo u : utterances) {
            sb.append(u.getText()).append(" ");
        }

        // 3. 특수문자 제거
        String cleanedText =
                sb.toString().replaceAll("[.,!?:;()\\[\\]{}'\"]", " ");

        // 4. 단어 분리
        String[] words = cleanedText.split("\\s+");

        // 5. 불용어 목록
        Set<String> stopWords = new HashSet<>();
        stopWords.add("진짜");
        stopWords.add("약간");
        stopWords.add("아마");
        stopWords.add("그리고");
        stopWords.add("하지만");
        stopWords.add("그래서");
        stopWords.add("저는");
        stopWords.add("제가");

        // 6. 키워드 빈도 계산
        Map<String, Integer> keywordMap = new HashMap<>();

        for (String word : words) {
            if (word.length() < 2) continue;
            if (stopWords.contains(word)) continue;

            if (keywordMap.containsKey(word)) {
                int count = keywordMap.get(word);
                keywordMap.put(word, count + 1);
            } else {
                keywordMap.put(word, 1);
            }
        }

        // 7. PriorityQueue로 Top-K 추출하기
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(new KeywordEntryComparator());

        // 8. 모든 키워드를 큐에 추가하면서 크기 제한하기
        for (Map.Entry<String, Integer> entry : keywordMap.entrySet()) {
            pq.offer(entry);
            if (pq.size() > limit) {
                pq.poll();
            }
        }

        // 9. 결과 변환
        List<KeywordStat> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            Map.Entry<String, Integer> entry = pq.poll();
            result.add(new KeywordStat(entry.getKey(), entry.getValue()));
        }

        // 10. 내림차순 정렬
        Collections.reverse(result);

        return result;
    }
}
