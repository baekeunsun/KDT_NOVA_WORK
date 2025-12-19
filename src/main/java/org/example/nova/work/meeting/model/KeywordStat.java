package org.example.nova.work.meeting.model;


import lombok.Data;

@Data
public class KeywordStat {
    private String keyword;
    private int count;

    public KeywordStat(String keyword, int count) {
        this.keyword = keyword;
        this.count = count;
    }
}