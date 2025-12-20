CREATE DATABASE IF NOT EXISTS work_db
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE work_db;

-- work_db.T_MEETING_BASE definition

CREATE TABLE `T_MEETING_BASE` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT '회의 ID',
  `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '회의명',
  `STATUS` int DEFAULT '0',
  `ATTENDEE_LIST` longtext COMMENT '참석자 명단',
  `AUDIO_URL` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'S3 버킷 업로드 주소',
  `CRT_DTIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '회의 생성 시각',
  `END_DTIME` datetime DEFAULT NULL COMMENT '회의 종료 시각',
  `CRT_ID` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '회의 생성자 ID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=913 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='회의 기본 정보';

-- work_db.T_MEETING_FINAL_SUMMARY definition

CREATE TABLE `T_MEETING_FINAL_SUMMARY` (
  `id` varchar(100) NOT NULL,
  `MEETING_ID` int NOT NULL COMMENT '회의 ID',
  `TITLE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '주제 제목',
  `CONTENT` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '주제 내용',
  `CONCLUSION` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '주제 결론',
  `CRT_DTIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
  `MOD_DTIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정 시간',
  `MOD_ID` varchar(15) NOT NULL DEFAULT 'SYSTEM' COMMENT '최종 수정자',
  PRIMARY KEY (`id`,`MEETING_ID`),
  KEY `FK_T_MEETING_BASE_TO_T_MEETING_FINAL_SUMMARY` (`MEETING_ID`),
  CONSTRAINT `FK_T_MEETING_BASE_TO_T_MEETING_FINAL_SUMMARY` FOREIGN KEY (`MEETING_ID`) REFERENCES `T_MEETING_BASE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='회의 최종 요약 정보';

-- work_db.T_MEETING_TODO_INFO definition

CREATE TABLE `T_MEETING_TODO_INFO` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT '투두 ID',
  `MEETING_ID` int NOT NULL COMMENT '회의 ID',
  `TODO_DESC` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '요청사항',
  `ASSIGNEE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '담당자',
  `DUE_DATE` date DEFAULT NULL COMMENT '마감일',
  `DUE_DATE_TEXT` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '마감일 문자형',
  `CRT_DTIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
  PRIMARY KEY (`ID`),
  KEY `FK_T_MEETING_BASE_TO_T_MEETING_TODO_INFO` (`MEETING_ID`),
  CONSTRAINT `FK_T_MEETING_BASE_TO_T_MEETING_TODO_INFO` FOREIGN KEY (`MEETING_ID`) REFERENCES `T_MEETING_BASE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=87136 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='To-Do 정보';

-- work_db.T_MEETING_UTTERANCE_INFO definition

CREATE TABLE `T_MEETING_UTTERANCE_INFO` (
  `ID` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `MEETING_ID` int NOT NULL COMMENT '회의 ID',
  `IDX` int NOT NULL COMMENT '회의 내 발화 시퀀스',
  `SPEAKER_LABEL` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'AI에서 제공한 발화자 식별자',
  `SPEAKER_NAME` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '발화자 명칭',
  `TEXT` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '발화 내용',
  `START_DTIME` float NOT NULL COMMENT '회의 시작 시점으로 부터 발화가 시작된 시간차(초)',
  `END_DTIME` float NOT NULL COMMENT '회의 시작 시점으로 부터 발화가 종료된 시간차(초)',
  `CRT_DTIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
  `MOD_DTIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정 시간',
  `MOD_ID` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'SYSTEM' COMMENT '최종 수정자',
  PRIMARY KEY (`ID`,`MEETING_ID`),
  KEY `FK_T_MEETING_BASE_TO_T_MEETING_UTTERANCE_INFO` (`MEETING_ID`),
  CONSTRAINT `FK_T_MEETING_BASE_TO_T_MEETING_UTTERANCE_INFO` FOREIGN KEY (`MEETING_ID`) REFERENCES `T_MEETING_BASE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='발화 정보';

-- work_db.T_MEETING_VIEWER_MAP definition

CREATE TABLE `T_MEETING_VIEWER_MAP` (
  `VIEWER_ID` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '공유받은 사용자 ID',
  `MEETING_ID` int NOT NULL COMMENT '공유된 회의 ID',
  `CRT_DTIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '공유 추가 시각',
  PRIMARY KEY (`VIEWER_ID`,`MEETING_ID`),
  KEY `FK_T_MEETING_BASE_TO_T_MEETING_VIEWER_MAP` (`MEETING_ID`),
  CONSTRAINT `FK_T_MEETING_BASE_TO_T_MEETING_VIEWER_MAP` FOREIGN KEY (`MEETING_ID`) REFERENCES `T_MEETING_BASE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='공유 회의 뷰어(읽기 전용 사용자) 맵핑';

-- ======================================================================
-- 테스트용 DML (코딩테스트 준비용 기본 데이터)
-- ======================================================================

-- Safe Update Mode 일시적으로 비활성화 (전체 삭제를 위해 필요)
SET SQL_SAFE_UPDATES = 0;

-- 기존 데이터 정리 (있다면 모두 삭제)
DELETE FROM T_MEETING_VIEWER_MAP;
DELETE FROM T_MEETING_UTTERANCE_INFO;
DELETE FROM T_MEETING_TODO_INFO;
DELETE FROM T_MEETING_FINAL_SUMMARY;
DELETE FROM T_MEETING_BASE;

-- Safe Update Mode 다시 활성화
SET SQL_SAFE_UPDATES = 1;

-- 회의 기본 정보 (2건)
INSERT INTO T_MEETING_BASE
  (ID, NAME, STATUS, ATTENDEE_LIST, AUDIO_URL, CRT_DTIME, END_DTIME, CRT_ID)
VALUES
  (1, 'AI 회의 분석 PoC', 1, '홍길동,김철수,이영희,박민수,최지연', 'https://s3.bucket/meeting1/audio.mp3',
   '2024-12-20 10:00:00', '2024-12-20 11:00:00', 'admin'),
  (2, '마케팅 전략 회의', 1, '박민수,최지연', 'https://s3.bucket/meeting2/audio.mp3',
   '2024-12-21 14:00:00', '2024-12-21 15:00:00', 'admin');

-- 회의 1번 발화 데이터 (Top-K 연습용)
-- 발화자별 건수/시간 차이를 명확히 하여 복합 정렬 테스트 가능하도록 구성
-- 예상 결과:
--   1순위: 홍길동 (5건, 120초) - 건수 가장 많음
--   2순위: 김철수 (3건, 90초) - 건수 2번째, 시간도 많음
--   3순위: 이영희 (3건, 45초) - 건수 같지만 시간 적음 (시간으로 정렬)
--   4순위: 박민수 (2건, 30초)
--   5순위: 최지연 (1건, 15초)
INSERT INTO T_MEETING_UTTERANCE_INFO
  (ID, MEETING_ID, IDX, SPEAKER_LABEL, SPEAKER_NAME, TEXT, START_DTIME, END_DTIME, CRT_DTIME, MOD_DTIME, MOD_ID)
VALUES
  -- 홍길동: 5건, 총 120초 (가장 많이 발화)
  ('utt-1', 1, 1, 'spk_1', '홍길동', '프로젝트 일정과 역할을 먼저 정리하겠습니다. 프로젝트 진행 방향을 논의해야 합니다.', 0.0, 25.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-5', 1, 5, 'spk_1', '홍길동', '알고리즘 구현 방식을 결정해야 합니다. 우선순위 큐를 사용하는 것이 좋겠습니다.', 50.0, 70.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-7', 1, 7, 'spk_1', '홍길동', '데이터베이스 설계도 함께 검토하겠습니다. 데이터 구조를 먼저 확인하겠습니다.', 85.0, 100.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-10', 1, 10, 'spk_1', '홍길동', '프로젝트 마감일을 확인하고 일정을 조정하겠습니다.', 120.0, 135.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-13', 1, 13, 'spk_1', '홍길동', '마지막으로 전체 프로젝트 진행 상황을 점검하겠습니다.', 150.0, 165.0, NOW(), NOW(), 'SYSTEM'),

  -- 김철수: 3건, 총 90초 (건수 2번째, 시간도 많음)
  ('utt-2', 1, 2, 'spk_2', '김철수', '백엔드 API 개발을 담당하겠습니다. 데이터베이스 연동도 함께 진행하겠습니다.', 26.0, 50.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-6', 1, 6, 'spk_2', '김철수', '알고리즘 성능을 최적화해야 합니다. 데이터 처리 속도를 개선하겠습니다.', 71.0, 84.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-11', 1, 11, 'spk_2', '김철수', '프로젝트 테스트 코드를 작성하겠습니다. 코드 리뷰도 진행하겠습니다.', 136.0, 151.0, NOW(), NOW(), 'SYSTEM'),

  -- 이영희: 3건, 총 45초 (건수 같지만 시간 적음 - 이름으로 정렬)
  ('utt-3', 1, 3, 'spk_3', '이영희', '프론트엔드 화면 설계를 맡겠습니다.', 51.0, 60.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-8', 1, 8, 'spk_3', '이영희', 'UI 컴포넌트를 개발하겠습니다.', 101.0, 110.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-14', 1, 14, 'spk_3', '이영희', '사용자 경험을 개선하겠습니다.', 166.0, 175.0, NOW(), NOW(), 'SYSTEM'),

  -- 박민수: 2건, 총 30초
  ('utt-4', 1, 4, 'spk_4', '박민수', '데이터 분석 결과를 공유하겠습니다.', 61.0, 75.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-9', 1, 9, 'spk_4', '박민수', '성능 테스트 결과를 확인하겠습니다.', 111.0, 120.0, NOW(), NOW(), 'SYSTEM'),

  -- 최지연: 1건, 총 15초 (가장 적게 발화)
  ('utt-12', 1, 12, 'spk_5', '최지연', '문서화 작업을 진행하겠습니다.', 152.0, 167.0, NOW(), NOW(), 'SYSTEM');

-- 회의 2번 발화 데이터 (요약/투두 없는 케이스, 키워드 빈도 테스트용)
-- 키워드 반복을 명확히 하여 키워드 Top K 테스트 가능하도록 구성
-- 주요 키워드: "마케팅" (3회), "예산" (3회), "데이터" (2회), "리포트" (2회)
INSERT INTO T_MEETING_UTTERANCE_INFO
  (ID, MEETING_ID, IDX, SPEAKER_LABEL, SPEAKER_NAME, TEXT, START_DTIME, END_DTIME, CRT_DTIME, MOD_DTIME, MOD_ID)
VALUES
  ('utt-15', 2, 1, 'spk_1', '박민수', '이번 분기 마케팅 목표를 먼저 공유드리겠습니다. 마케팅 전략을 수립해야 합니다.', 0.0, 15.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-16', 2, 2, 'spk_2', '최지연', '온라인 채널 예산을 조금 더 늘리는 안을 제안드립니다. 예산 배분을 재검토하겠습니다.', 16.0, 32.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-17', 2, 3, 'spk_1', '박민수', '좋습니다. 데이터 리포트를 기반으로 다시 검토하겠습니다. 리포트 분석 결과를 확인하겠습니다.', 33.0, 48.0, NOW(), NOW(), 'SYSTEM'),
  ('utt-18', 2, 4, 'spk_2', '최지연', '마케팅 캠페인 성과를 데이터로 분석하겠습니다. 예산 효율성을 평가하겠습니다.', 49.0, 62.0, NOW(), NOW(), 'SYSTEM');

-- 회의 1번 최종 요약 존재 (Boolean 테스트용)
INSERT INTO T_MEETING_FINAL_SUMMARY
  (id, MEETING_ID, TITLE, CONTENT, CONCLUSION, CRT_DTIME, MOD_DTIME, MOD_ID)
VALUES
  ('fs-1', 1, 'AI 회의 분석 PoC 요약',
   '회의 발화 데이터를 기반으로 Top K 발화자와 키워드 리포트를 제공하기로 합의하였다.',
   '우선순위 큐를 활용한 Top K 알고리즘을 백엔드 서비스에 구현한다.',
   NOW(), NOW(), 'SYSTEM');

-- 회의 1번 TODO 존재 (Boolean 테스트용)
INSERT INTO T_MEETING_TODO_INFO
  (ID, MEETING_ID, TODO_DESC, ASSIGNEE_ID, DUE_DATE, DUE_DATE_TEXT, CRT_DTIME)
VALUES
  (1, 1, 'Top K 알고리즘 초안 구현', 'dev_backend', '2024-12-25', '12월 25일까지', NOW()),
  (2, 1, '회의 리포트 화면 와이어프레임 작성', 'dev_front', '2024-12-26', '12월 26일까지', NOW());

-- 회의 2번은 요약/투두 없음 (false 케이스)

-- 회의 뷰어 맵핑 (공유 사용자 수 테스트용)
INSERT INTO T_MEETING_VIEWER_MAP
  (VIEWER_ID, MEETING_ID, CRT_DTIME)
VALUES
  ('viewer01', 1, NOW()),
  ('viewer02', 1, NOW()),
  ('viewer03', 1, NOW()),
  ('viewer01', 2, NOW());
