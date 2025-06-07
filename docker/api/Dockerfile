#docker/api/Dockerfile

# 1단계: Gradle 빌드
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Gradle 멀티모듈 프로젝트 구조 복사
COPY settings.gradle ./settings.gradle
COPY common ./common
COPY data/api .data/api

# api 디렉토리로 이동
RUN gradle :data:api:bootJar --no-daemon

# 2단계: 실행용 경량 이미지
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드된 JAR 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 업로드 디렉토리 생성 (권한 포함)
RUN mkdir -p /tmp/uploads && chmod 777 /tmp/uploads

ENTRYPOINT ["java", "-jar", "app.jar"]
