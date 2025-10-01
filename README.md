# SampleCodeProject

LocalStack을 이용한 AWS 서비스 테스트 프로젝트입니다.

## 프로젝트 개요

이 프로젝트는 LocalStack을 사용하여 로컬 환경에서 AWS 서비스를 테스트하는 예제 코드를 제공합니다.
다음 AWS 서비스들의 기본적인 사용법을 Java로 구현한 테스트 코드가 포함되어 있습니다:

-   **DynamoDB**: NoSQL 데이터베이스 서비스
-   **S3**: 객체 스토리지 서비스
-   **SNS**: 알림 서비스
-   **SQS**: 메시지 큐 서비스

## 프로젝트 구조

```
SampleCodeProject/
├── README.md                        # 프로젝트 설명 문서
├── SampleCodeProject.iml           # IntelliJ IDEA 모듈 파일
├── file/
│   └── sample.txt                  # S3 업로드 테스트용 샘플 파일
├── localstack/
│   ├── docker-compose.yml          # LocalStack Docker 설정
│   └── init/
│       └── init.sh                 # LocalStack 초기화 스크립트
└── src/
    ├── LocalstackDynamoDBTest.java # DynamoDB 테스트 코드
    ├── LocalstackS3Test.java       # S3 테스트 코드
    ├── LocalstackSnsTest.java      # SNS 테스트 코드
    └── LocalstackSqsTest.java      # SQS 테스트 코드
```

## 시작하기 전에

### 필요 조건

-   Java 8 이상
-   Docker 및 Docker Compose
-   AWS SDK for Java

### 의존성

AWS SDK for Java를 프로젝트에 추가해야 합니다:

```xml
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-java-sdk</artifactId>
    <version>1.12.x</version>
</dependency>
```

## 실행 방법

### 1. LocalStack 시작

LocalStack 컨테이너를 시작합니다:

```bash
cd localstack
docker-compose up -d
```

### 2. AWS 리소스 초기화

LocalStack이 시작되면 초기화 스크립트가 자동으로 실행되어 다음 리소스들을 생성합니다:

-   **S3 버킷**: `test-bucket`
-   **DynamoDB 테이블**: `user` (mallId, userId를 키로 사용)
-   **SNS 토픽**: `lunasoft-topic`, `gng-topic`
-   **SQS 큐**: `dean-queue`, `brian-queue`, `brandon-queue`, `jacob-queue`, `neo-queue`, `harvey-queue`

### 3. 테스트 코드 실행

각 서비스별 테스트 코드를 실행할 수 있습니다:

```bash
# DynamoDB 테스트
java -cp . LocalstackDynamoDBTest

# S3 테스트
java -cp . LocalstackS3Test

# SNS 테스트
java -cp . LocalstackSnsTest

# SQS 테스트
java -cp . LocalstackSqsTest
```

## 서비스별 기능

### DynamoDB 테스트 (`LocalstackDynamoDBTest.java`)

-   테이블 목록 조회
-   아이템 생성, 조회, 삭제
-   쿼리 실행

### S3 테스트 (`LocalstackS3Test.java`)

-   파일 업로드
-   파일 다운로드
-   버킷 및 객체 관리

### SNS 테스트 (`LocalstackSnsTest.java`)

-   토픽에 메시지 발행
-   토픽 구독 관리

### SQS 테스트 (`LocalstackSqsTest.java`)

-   큐에 메시지 전송
-   큐에서 메시지 수신
-   메시지 삭제

## 연결 설정

모든 테스트 코드는 다음 LocalStack 엔드포인트를 사용합니다:

-   **엔드포인트**: `http://localhost:4566`
-   **리전**: `ap-northeast-2`
-   **액세스 키**: `test-access-key`
-   **시크릿 키**: `test-secret-key`

## 종료

테스트 완료 후 LocalStack 컨테이너를 정리합니다:

```bash
docker-compose down
```

## 참고사항

-   LocalStack은 AWS 서비스를 로컬에서 에뮬레이션하므로 실제 AWS와 동일하지 않을 수 있습니다.
-   테스트용 크레덴셜을 사용하므로 실제 AWS 계정에는 영향을 주지 않습니다.
-   프로덕션 환경에서는 적절한 AWS 크레덴셜과 엔드포인트를 사용해야 합니다.
