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

#### 2️⃣ 회의 발화 분석 리포트 API (`GET /work/meeting/{meetingId}/report`)

회의 내 대량의 발화 데이터를 분석하여 상위 발화자와 키워드를 추출합니다. (Top-K 알고리즘 핵심)

* **Query Parameter**: `speakerLimit` (상위 발화자 수), `keywordLimit` (상위 키워드 수)

---

### 🔥 핵심 기능 상세 요구 사항

#### 1. 발화자 Top-N 분석 (Priority Queue 활용)

* **대상**: `T_MEETING_UTTERANCE_INFO` 테이블의 전체 발화 데이터.
* **집계 항목**: 발화자별 총 발화 건수, 총 발화 시간(END - START).
* **복합 정렬 기준 (우선순위 큐 적용)**:
1. **발화 건수** 내림차순
2. **발화 시간 합** 내림차순
3. **발화자 이름** 오름차순 (가나다순)


* **구현 가이드**: 전체 발화자를 Map으로 집계한 후, `PriorityQueue`를 사용하여 상위 `speakerLimit` 명을 효율적으로 선별하세요.

#### 2. 키워드 Top-M 분석 (Top-K 알고리즘)

* **대상**: 발화 텍스트(`TEXT`) 전체.
* **데이터 처리 규칙**:
* 공백 기준 분리 및 특수문자(`. , ! ? : ; ( ) [ ] { } " '`) 제거.
* 길이 2자 이상인 단어만 키워드로 간주.
* **불용어 제외**: `진짜, 약간, 아마, 그리고, 하지만, 그래서, 저는, 제가`


* **복합 정렬 기준**:
1. **등장 빈도수** 내림차순
2. **키워드 사전순** 오름차순


* **구현 가이드**: HashMap으로 빈도를 측정하고, `PriorityQueue`에 담아 상위 `keywordLimit` 개를 반환하세요.

#### 3. 요약/투두 존재 여부

* `T_MEETING_FINAL_SUMMARY`, `T_MEETING_TODO_INFO` 테이블에 데이터가 존재하는지 boolean으로 반환.

---

### 📁 권장 패키지 구조

```text
work.meeting
├── controller
│   └── MeetingReportController.java
├── service
│   ├── MeetingService.java (회의 검증 및 조회)
│   └── AnalysisService.java (Top-K 알고리즘 및 데이터 가공)
├── mapper
│   ├── MeetingMapper.java
│   └── MeetingMapper.xml (단순 Select 쿼리 위주)
└── model
    ├── MeetingInfoRes.java
    ├── MeetingReportRes.java
    ├── SpeakerStat.java (Comparable 구현 권장)
    └── KeywordStat.java

```

---

### 🎯 평가 포인트

1. **알고리즘 적정성**: Top-K 문제를 해결하기 위해 `PriorityQueue`의 특성을 정확히 활용했는가?
2. **복합 정렬 구현**: `Comparator` 또는 `Comparable`을 사용하여 여러 정렬 조건을 실수 없이 구현했는가?
3. **코드 클린도**: Service 레이어의 로직이 가독성 있게 분리되었으며, Stream API 등을 적절히 사용했는가?
4. **효율성**: 모든 데이터를 리스트에 넣고 무거운 `sort()`를 반복하는 대신, 힙(Heap) 구조를 통해 성능 최적화를 고려했는가?