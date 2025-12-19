package org.example.nova.work.meeting.util;

import org.example.nova.work.meeting.model.SpeakerStat;

import java.util.Comparator;

public class SpeakerStatComparator implements Comparator<SpeakerStat> {

    @Override
    public int compare(SpeakerStat s1, SpeakerStat s2) {

        // 1순위: 발화 건수 내림차순
        if (s1.getCount() != s2.getCount()) {
            return s2.getCount() - s1.getCount();
        }

        // 2순위: 이름 오름차순
        return s1.getSpeakerName().compareTo(s2.getSpeakerName());
    }
}