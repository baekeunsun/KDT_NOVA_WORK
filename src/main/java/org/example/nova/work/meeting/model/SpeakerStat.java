package org.example.nova.work.meeting.model;

import lombok.Data;

@Data
public class SpeakerStat {

    private String speakerName;  // 발화자 이름
    private int count;           // 발화 건수

    public SpeakerStat(String speakerName) {
        this.speakerName = speakerName;
        this.count = 0;
    }

    // 발화 건수 증가
    public void addCount() {
        this.count++;
    }
}
