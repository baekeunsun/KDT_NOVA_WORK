package org.example.nova.work.meeting.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MeetingBase {

    private int id;                 // ID
    private String name;             // 회의명
    private int status;              // 상태
    private String attendeeList;     // 참석자 명단
    private String audioUrl;          // 음성 파일 URL
    private LocalDateTime crtDtime;   // 생성 시각
    private LocalDateTime endDtime;   // 종료 시각
    private String crtId;             // 생성자 ID
}
