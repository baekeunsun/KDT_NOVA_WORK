package org.example.nova.work.meeting.model;

import lombok.Data;

@Data
public class UtteranceInfo {

    private String id;
    private int meetingId;
    private int idx;
    private String speakerLabel;
    private String speakerName;
    private String text;
}