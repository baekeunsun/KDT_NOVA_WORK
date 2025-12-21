# 🧠 회의 발화 데이터 기반 Top-K 분석 API 구현

### 📌 과제 목적

본 과제는 **'이로기(work 모듈)'**의 실제 도메인을 기반으로, 회의 종료 후 관리자/사용자에게 제공되는 **회의 발화 분석 리포트 API**를 구현하는 것이 목적입니다.

단순한 기능 구현을 넘어, 수업 시간에 학습한 **우선순위 큐(Priority Queue)와 힙(Heap)** 자료구조를 활용하여 대량의 발화 데이터에서 상위 K개의 유의미한 정보를 효율적으로 추출하는 능력을 종합적으로 연습합니다.

---

### 🔍 진행 방식

* **핵심 역량**: Layered Architecture 이해, 복합 정렬 조건 구현, **Top-K 알고리즘(PQ)** 적용.
* **🚨 0점 방지 체크리스트**:
* Mapper(XML) 내에서의 **집계(GROUP BY) 및 정렬(ORDER BY)은 절대 금지**합니다.
* 모든 통계 및 정렬 로직은 **Java Service 레이어**에서 직접 구현해야 합니다.
* 컴파일 오류 또는 실행 불가 상태는 오답 처리됩니다.
* **API 3번은 반드시 `PriorityQueue`를 사용해야 합니다.**



---

### 🏗️ 구현 범위 및 기술 스택

* **Language**: Java 11 이상
* **Framework**: Spring Boot
* **DB/ORM**: MyBatis (Mapper Interface + XML)
* **계층 구조**: Controller / Service / Mapper / DTO 전체 구현

---

### 🧩 구현해야 할 API 목록

#### 1️⃣ 회의 기본 정보 조회 API (`GET /work/meeting/{meetingId}`)

회의 상세 화면 진입 시 사용되는 기본 정보와 공유받은 사용자의 수를 조회합니다.

* **사용 테이블**: `T_MEETING_BASE`, `T_MEETING_VIEWER_MAP`
* **구현 내용**: 회의 ID 기준 정보 조회 및 공유받은 사용자의 수 계산(COUNT SQL 허용).
* **예외 처리**: 회의가 존재하지 않을 경우 적절한 Custom Exception 처리.

#### 2️⃣ 회의 발화자 Top-K 조회 API (`GET /work/meeting/{meetingId}/speakers`)

회의 내 발화 데이터를 분석하여 상위 발화자를 추출합니다.

* **사용 테이블**: `T_MEETING_UTTERANCE_INFO`
* **Query Parameter**: `limit` (상위 발화자 수, 기본값: 3)
* **구현 내용**:
  * 회의 ID 기준으로 모든 발화 데이터 조회
  * 발화자 이름을 기준으로 발화 횟수 집계
  * 발화 횟수를 기준으로 상위 발화자 추출
  * 우선순위 큐(PriorityQueue)를 활용하여 Top-K 발화자 유지
* **정렬 기준**:
  * 1순위: 발화 건수 내림차순
  * 2순위: 발화자 이름 오름차순

#### 3️⃣ 회의 키워드 Top-K 조회 API (`GET /work/meeting/{meetingId}/keywords`)

회의 내 발화 텍스트에서 상위 키워드를 추출합니다.

* **사용 테이블**: `T_MEETING_UTTERANCE_INFO`
* **Query Parameter**: `limit` (상위 키워드 수, 기본값: 5)
* **구현 내용**:
  * 회의 ID 기준으로 모든 발화 텍스트 조회
  * 발화 텍스트 전처리 후 키워드 빈도 분석
    * 특수문자 제거 후 분석  
      (제거 대상 예시: `. , ! ? : ; ( ) [ ] { } ' "`)
    * 공백 기준으로 단어 분리
    * 의미 없는 단어(불용어)는 분석 대상에서 제외  
      (불용어 예시: `"진짜"`, `"약간"`, `"아마"`, `"그리고"`, `"하지만"`, `"그래서"`, `"저는"`, `"제가"`)
    * 길이가 1자인 단어는 제외
  * 키워드별 등장 빈도 집계
  * 우선순위 큐(PriorityQueue)를 활용하여 상위 `limit`개의 키워드 추출
* **정렬 기준**:
  * 1순위: 키워드 등장 빈도 내림차순
  * 2순위: 키워드 오름차순

---

### 🎯 평가 포인트

1. **우선순위 큐 활용**: API 3번에서 `PriorityQueue`를 정확히 활용하여 Top-K 문제를 효율적으로 해결했는가?
2. **복합 정렬 구현**: `Comparator` 또는 `Comparable`을 사용하여 여러 정렬 조건을 실수 없이 구현했는가?
3. **코드 클린도**: Service 레이어의 로직이 가독성 있게 분리되었으며, 적절한 자료구조를 사용했는가?
4. **예외 처리**: 회의가 존재하지 않을 경우 적절한 예외 처리를 구현했는가?

### 📝 구현 난이도 안내

* **API 1번**: ⭐⭐ (기본 조회, COUNT 쿼리)
* **API 2번**: ⭐⭐⭐ (집계 + 일반 정렬)
* **API 3번**: ⭐⭐⭐⭐ (문자열 처리 + 우선순위 큐)

---

### 🔥 핵심 기능 상세 요구 사항 및 구현 가이드

---

## 📘 API 1번: 회의 기본 정보 조회

### 단계별 구현 가이드

#### 1단계: Mapper 인터페이스 만들기

```java
// MeetingMapper.java
package work.meeting.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import work.meeting.model.MeetingBase;

@Mapper
public interface MeetingMapper {
    // 회의 기본 정보 조회
    MeetingBase findById(@Param("meetingId") int meetingId);
    
    // 공유받은 사용자 수 조회 (COUNT 쿼리 허용)
    int countViewers(@Param("meetingId") int meetingId);
}
```

**설명**:
- `@Mapper`: MyBatis가 이 인터페이스를 인식하도록 하는 어노테이션
- `@Param`: SQL에 전달할 파라미터 이름 지정
- `findById`: 회의 ID로 회의 정보를 찾는 메서드
- `countViewers`: 해당 회의를 공유받은 사용자 수를 세는 메서드

#### 2단계: Mapper XML 작성

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="org.example.nova.work.meeting.mapper.MeetingMapper">  
    
    <!-- 회의 기본 정보 조회 -->
    <select id="findById" resultType="work.meeting.model.MeetingBase">
        <!-- TODO: T_MEETING_BASE 테이블에서 회의 정보 조회
             힌트: 
             - SELECT로 필요한 컬럼 조회
             - WHERE 절로 조건 추가
        -->
    </select>
    
    <!-- 공유받은 사용자 수 조회 -->
    <select id="countViewers" resultType="int">
        <!-- TODO: T_MEETING_VIEWER_MAP 테이블에서 사용자 수 세기
             힌트:
             - SELECT COUNT(*) 사용
             - WHERE 절로 조건 추가
        -->
    </select>
</mapper>
```

**설명**:
- `namespace`: Mapper 인터페이스의 전체 경로와 일치해야 함 (`work.meeting.mapper.MeetingMapper`)
- `resultType`: 조회 결과를 담을 클래스 타입 (findById는 `MeetingBase`, countViewers는 `int`)
- `#{meetingId}`: 파라미터로 전달받은 값 사용 (인터페이스의 `@Param("meetingId")`와 매칭)
- `id`: 인터페이스의 메서드 이름과 일치해야 함

#### 3단계: DTO 클래스(응답 클래스) 만들기

```java
// MeetingInfoRes.java
package work.meeting.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MeetingInfoRes {
    // TODO: 응답 파라미터를 적어주세요.
}
```

**설명**:
- `@Data`: Lombok 어노테이션으로 getter/setter 자동 생성

#### 4단계: Service 구현

```java
@Service
public class MeetingService {
    
    @Autowired
    private MeetingMapper meetingMapper;
    
    public MeetingInfoRes getMeetingInfo(int meetingId) {
        // TODO: 1. Mapper를 사용해서 회의 정보 조회
        
        // TODO: 2. 회의가 없으면 예외 발생 (if문 사용)
        
        // TODO: 3. Mapper를 사용해서 공유받은 사용자 수 조회
        
        // TODO: 4. MeetingInfoRes 객체를 만들고 데이터 채우기
        
        // TODO: 5. 결과 반환
    }
}
```

**힌트**:
- `meetingMapper.findById(meetingId)`로 회의 정보를 가져올 수 있어요
- 회의가 없으면 `null`이 반환됩니다. `if (meeting == null)`로 체크하세요
- `meetingMapper.countViewers(meetingId)`로 사용자 수를 가져올 수 있어요
- `new MeetingInfoRes()`로 객체를 만들고, `setXXX()` 메서드로 값을 채우세요

#### 5단계: Controller 구현

```java
// MeetingController.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.meeting.model.MeetingInfoRes;
import work.meeting.service.MeetingService;

@RestController
@RequestMapping("/work/meeting")
public class MeetingController {
    
    @Autowired
    private MeetingService meetingService;

    @Operation(summary = "1번: 회의 기본 정보 조회")
    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingInfoRes> getMeetingInfo(
            @PathVariable int meetingId
    ) {
        MeetingInfoRes response = meetingService.getMeetingInfo(meetingId);
        return ResponseEntity.ok(response);
    }
}
```

**설명**:
- `@RestController`: REST API 컨트롤러로 인식
- `@RequestMapping`: 기본 경로 설정
- `@Operation`: Swagger 문서에 노출되는 API 설명
- `@GetMapping("/{meetingId}")`: GET 요청 처리, 경로 변수 사용
- `ResponseEntity<T>`: HTTP 응답을 명확하게 표현
  → 상태 코드 + 응답 바디를 함께 다룰 수 있음
- `@PathVariable`: URL 경로에서 변수 추출

---

## 📗 API 2번: 발화자 Top-K 분석

### 단계별 구현 가이드

#### 1단계: 발화 데이터 조회용 Mapper 및 VO(발화 데이터 정보 클래스) 추가

```java
// MeetingMapper.java에 추가
List<UtteranceInfo> findUtterancesByMeetingId(@Param("meetingId") int meetingId);
```

```java
// UtteranceInfo.java (새로 만들기)
package org.example.nova.work.meeting.model;

import lombok.Data;

@Data
public class UtteranceInfo {
    // TODO : 발화 데이터에 필요할 것으로 추정되는 파라미터들을 적어주세요.
}
```

```xml
<!-- MeetingMapper.xml에 추가 -->
<select id="findUtterancesByMeetingId" resultType="work.meeting.model.UtteranceInfo">
    <!-- TODO: T_MEETING_UTTERANCE_INFO 테이블에서 발화 데이터 조회
         힌트:
         - SELECT로 필요한 컬럼 조회 (ID, MEETING_ID, IDX, SPEAKER_LABEL, SPEAKER_NAME, TEXT, START_DTIME, END_DTIME)
         - WHERE 절로 MEETING_ID = #{meetingId} 조건 추가
         - ⚠️ 주의: ORDER BY를 쓰지 않습니다! Java에서 정렬할 거예요
    -->
</select>
```

**설명**:
- 발화 데이터를 모두 가져옵니다
- **주의**: ORDER BY를 쓰지 않습니다! Java에서 정렬할 거예요

#### 2단계: 발화자 통계를 담을 클래스 만들기

```java
// SpeakerStat.java
package work.meeting.model;

import lombok.Data;

@Data
public class SpeakerStat {
    private String speakerName;  // 발화자 이름
    private int count;           // 발화 건수
    
    // 생성자
    public SpeakerStat(String speakerName) {
        this.speakerName = speakerName;
        this.count = 0;
    }
    
    // 발화 건수 증가
    public void addCount() {
        this.count++;
    }
}
```

**설명**:
- 각 발화자의 통계를 담는 클래스
- `addCount()`: 발화 건수를 1씩 증가
- 발화 건수만 집계하면 됩니다

#### 3단계: 발화자 정렬 기준(Comparator) 만들기

#### 3단계: Service에서 집계하기

```java
@Service
public class AnalysisService {
    
    @Autowired
    private MeetingMapper meetingMapper;
    
    public List<SpeakerStat> getTopSpeakers(int meetingId, int limit) {
        // TODO 1. 발화 데이터 모두 가져오기
        // 힌트: meetingMapper.findUtterancesByMeetingId() 사용
        
        // TODO 2. 발화자별 집계하기
        // 힌트: Map<String, SpeakerStat> speakerMap = new HashMap<>();
        //       for문으로 발화 데이터를 순회하면서:
        //       - 발화자 이름을 키로 사용
        //       - Map에 없으면 새로 만들기 (containsKey() 사용)
        //       - 발화 건수 증가 (addCount() 호출)
        // 아래 코드로 실행하시면, 정상작동합니다. 어떻게 되는건지 확인해보세요~~
        Map<String, SpeakerStat> speakerMap = new HashMap<>();
        
        for (UtteranceInfo utterance : utterances) {
            String speakerName = utterance.getSpeakerName();

            // Map에 없으면 새로 생성
            if (!speakerMap.containsKey(speakerName)) {
                speakerMap.put(speakerName, new SpeakerStat(speakerName));
            }

            // 발화 건수 증가
            speakerMap.get(speakerName).addCount();
        }
        
        // TODO 3. PriorityQueue 생성 (⭐ 핵심)
        // 힌트: 
        //       Comparator를 만들어서:
        //       1순위: 발화 건수 내림차순 (s2.getCount() - s1.getCount())
        //       2순위: 발화 시간 내림차순 (Float.compare() 사용)
        //       3순위: 이름 오름차순 (compareTo() 사용)
        
        // 5단계: 상위 limit 개만 반환하기
        // 힌트: subList(0, limit) 사용
    }
}
```

**핵심 개념 설명**:
- **Map 사용 이유**: 발화자 이름을 키로 사용해서 같은 발화자의 통계를 모을 수 있어요
- **HashMap**: 키-값 쌍을 저장하는 자료구조. `containsKey()`, `get()`, `put()` 메서드 사용
- **values()**: Map의 모든 값들을 가져오는 메서드
- **Collections.sort()**: 리스트를 정렬하는 메서드. Comparator로 정렬 기준 정의
- **내림차순**: `s2 - s1` 또는 `s2.compareTo(s1)` (큰 값이 앞에 옴)
- **오름차순**: `s1 - s2` 또는 `s1.compareTo(s2)` (작은 값이 앞에 옴)

#### 5단계: Controller에 추가

```java
// MeetingController.java에 추가
@Autowired
private MeetingReportService meetingReportService;

@Operation(summary = "2번: 회의 발화자 Top-K 조회")
@GetMapping("/{meetingId}/speakers")
public ResponseEntity<List<SpeakerStat>> getTopSpeakers(
        @PathVariable int meetingId,
        @RequestParam(defaultValue = "3") int limit) {
    return analysisService.getTopSpeakers(meetingId, limit);
}
```

**설명**:
- `@RequestParam`: 쿼리 파라미터 받기
- `defaultValue = "3"`: limit을 안 주면 기본값 3 사용

---

## 📙 API 3번: 키워드 Top-K 분석 (우선순위 큐)

### 단계별 구현 가이드

#### 1단계: 키워드 통계를 담을 클래스 만들기

```java
// KeywordStat.java
package work.meeting.model;

import lombok.Data;

@Data
public class KeywordStat {
    private String keyword;  // 키워드
    private int count;       // 등장 빈도수
    
    public KeywordStat(String keyword, int count) {
        this.keyword = keyword;
        this.count = count;
    }
}
```

#### 2단계: Service에서 키워드 분석하기

```java
public List<KeywordStat> getTopKeywords(int meetingId, int limit) {
    // TODO: 1. 발화 데이터 모두 가져오기
    // 힌트: meetingMapper.findUtterancesByMeetingId() 사용
    
    // TODO: 2. 모든 발화 텍스트를 합치기
    // 힌트: StringBuilder 사용, for문으로 텍스트를 append()
    StringBuilder sb = new StringBuilder();
    for (UtteranceInfo u : utterances) {
        sb.append(u.getText()).append(" ");
    }
    
    // TODO: 3. 특수문자 제거하기
    // 힌트: replaceAll("[.,!?:;()\\[\\]{}'\"]", " ") 사용
    // 위의 sb를 String으로 만드신 다음에 replace 사용하시면 됩니다!
    // ex) sb.toString().replaceAll ...
    
    // TODO: 4. 공백 기준으로 단어 분리하기
    // 힌트: split("\\s+") 사용
    // 위의 특수문자를 제거한 문자열에 split("\\s+")를 해주시면 단어가 분리됩니다!
    String[] words = {특수문자제거문자열}.split("\\s+");
    // split()의 리턴 타입이 원래 String[]이기 때문에 List<>로 사용하지 않고 그냥 String[]을 사용하였습니다.
    // 추가로 공부해보아도 좋을 것 같아요~~
    
    // TODO: 5. 불용어 목록 만들기
    // 힌트: Set<String> stopWords = new HashSet<>() 사용
    //       불용어: "진짜", "약간", "아마", "그리고", "하지만", "그래서", "저는", "제가"
    
    // TODO: 6. HashMap으로 키워드별 빈도 계산하기
    // 힌트: Map<String, Integer> keywordMap = new HashMap<>();
    //       for문으로 단어를 순회하면서:
    //       - 길이 2자 이상인지 체크 (length() < 2면 continue)
    //       - 불용어인지 체크 (stopWords.contains()면 continue)
    //       - 빈도수 증가 (Map에서 해당 키의 count값(value)을 받아와서, put해줄때 count+1을 하면 되겠죠? 혹은 SpeakStat처럼 클래스 내부 메서드로 구현해도 좋아요~)
    
    // TODO: 7. PriorityQueue로 Top-K 추출하기 (핵심!)
    // 힌트: PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(comparator);
    //       Comparator 만들기:
    //       1순위: 빈도수 내림차순 (e2.getValue() - e1.getValue())
    //       2순위: 키워드 사전순 오름차순 (e1.getKey().compareTo(e2.getKey()))
    
    // TODO: 8. 모든 키워드를 큐에 추가하면서 크기 제한하기
    // 힌트: for문으로 keywordMap.entrySet() 순회
    //       - pq.offer(entry)로 추가
    //       - if (pq.size() > limit) pq.poll()로 가장 작은 것 제거
    
    // TODO: 9. 결과를 List로 변환하기
    // 힌트: while (!pq.isEmpty())로 poll()해서 List에 추가
    
    // TODO: 10. 내람차순으로 정렬 (빈도수 높은 것부터, 필수아님~)
    // 힌트: Collections.reverse() 사용
}
```

**핵심 개념 설명**:
- **StringBuilder**: 문자열을 효율적으로 합치기 위해 사용. `append()` 메서드로 추가
- **replaceAll()**: 정규식으로 특수문자 제거. `[.,!?:;()\\[\\]{}'\"]` 패턴 사용
- **split("\\s+")**: 공백 하나 이상으로 문자열 분리
- **HashSet**: 불용어를 빠르게 찾기 위해 사용. `contains()` 메서드로 확인
- **getOrDefault()**: Map에 키가 없으면 기본값(0) 반환, 있으면 값 반환
- **PriorityQueue**: 우선순위 큐. `offer()`로 추가, `poll()`로 제거, `isEmpty()`로 확인
- **Collections.reverse()**: 리스트 순서 뒤집기

**왜 PriorityQueue를 쓰나요?**
- 모든 데이터를 정렬하면 O(n log n) 시간이 걸려요
- PriorityQueue를 쓰면 O(n log k) 시간만 걸려요 (k는 limit)
- 데이터가 많을수록 훨씬 빠릅니다!
- 수업 시간에 배운 Top-K 알고리즘 패턴을 그대로 적용하면 됩니다!

#### 3단계: Controller에 추가

```java
// MeetingController.java에 추가
@Operation(summary = "3번 : 회의 키워드 Top-K 조회")
@GetMapping("/{meetingId}/keywords")
public ResponseEntity<List<KeywordStat>> getTopKeywords(
        @PathVariable int meetingId,
        @RequestParam(defaultValue = "5") int limit
) {
  List<KeywordStat> result =
          meetingReportService.getTopKeywords(meetingId, limit);

  return ResponseEntity.ok(result);
}
```

---

### ⚠️ 주의사항

1. **Mapper에서 ORDER BY 사용 금지**: 모든 정렬은 Java에서 해야 합니다!
2. **GROUP BY 사용 금지**: 집계도 Java에서 해야 합니다!
4. **예외 처리**: 회의가 없을 때 적절한 예외를 던져야 합니다
5. **null 체크**: 데이터가 없을 때를 대비해서 null 체크를 해야 합니다

---

### 📁 예시 패키지 구조

```text
work.meeting
├── controller
│   └── MeetingController.java
├── service
│   ├── MeetingService.java (회의 검증 및 조회)
│   └── AnalysisService.java (발화자/키워드 분석)
├── mapper
│   ├── MeetingMapper.java
│   └── MeetingMapper.xml (실제는 resource에 있지만 표현상 여기 적어두었습니다.)
├── util/Comparator
│   ├── SpeakerStatComparator.java (발화자 정렬)
│   └── KeywordEntryComparator.java (키워드 정렬)
└── model
    ├── MeetingInfoRes.java
    ├── ...

```

---

### 🎯 평가 포인트

#### 필수 구현 항목 (기본 점수)
1. **API 1번**: 회의 기본 정보 조회 및 공유 사용자 수 계산
2. **API 2번**: 발화자 Top-K 분석 (일반 정렬 사용)
3. **API 3번**: 키워드 Top-K 분석 (우선순위 큐 사용)

#### 추가 평가 포인트 (가산 점수)
1. **우선순위 큐 활용**: API 3번에서 `PriorityQueue`를 정확히 활용하여 Top-K 문제를 효율적으로 해결했는가?
2. **복합 정렬 구현**: `Comparator` 또는 `Comparable`을 사용하여 여러 정렬 조건을 실수 없이 구현했는가?
3. **코드 클린도**: Service 레이어의 로직이 가독성 있게 분리되었으며, 적절한 자료구조를 사용했는가?
4. **예외 처리**: 회의가 존재하지 않을 경우 적절한 예외 처리를 구현했는가?
