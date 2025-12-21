package org.example.nova.work.meeting.util;

import org.example.nova.work.meeting.model.SpeakerStat;

import java.util.Comparator;

public class SpeakerStatComparator implements Comparator<SpeakerStat> {

    @Override
    public int compare(SpeakerStat s1, SpeakerStat s2) {

        // 1순위: 발화 건수 오름차순 (그래야 가장 작은 수가 버려짐)
        if (s1.getCount() != s2.getCount()) {
            return s1.getCount() - s2.getCount();
        }

        // 2순위: 이름 내림차순 (사전상 뒤에 있는 사람이 먼저 나가게)
        return s2.getSpeakerName().compareTo(s1.getSpeakerName());
    }
}