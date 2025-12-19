package org.example.nova.work.meeting.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MeetingInfoRes {

    private int meetingId;
    private String name;
    private String attendeeList;
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;
    private String creatorId;
    private int viewerCount;
}
