package org.example.nova.work.meeting.util;

import java.util.Comparator;
import java.util.Map;

public class KeywordEntryComparator implements Comparator<Map.Entry<String, Integer>> {

    @Override
    public int compare(Map.Entry<String, Integer> e1,
                       Map.Entry<String, Integer> e2) {

        // 1순위: 빈도수 오름차순 (그래야 가장 작은 수가 버려짐)
        if (e1.getValue() != e2.getValue()) {
            return e1.getValue() - e2.getValue();
        }

        // 2순위: 키워드 내림차순 (사전상 뒤에 있는 키워드가 먼저 나가게)
        return e2.getKey().compareTo(e1.getKey());
    }
}