#docker/api/Dockerfile

# 1단계: Gradle 빌드
FROM gradle:7.6-jdk11 AS build
WORKDIR /app

# Gradle 멀티모듈 프로젝트 구조 복사
COPY settings.gradle .
COPY common ./common
COPY data/api ./data/api

# Gradle 설정
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.internal.http.connectionTimeout=180000 -Dorg.gradle.internal.http.socketTimeout=180000"
ENV GRADLE_USER_HOME=/app/.gradle

# Gradle 래퍼 권한 설정 및 빌드
RUN chmod +x ./data/api/gradlew
WORKDIR /app/data/api

# Gradle 캐시 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/.gradle && chmod 777 /app/.gradle

# 빌드 실행
RUN ./gradlew bootJar --no-daemon --stacktrace

# 빌드 결과물 확인
RUN ls -la build/libs/

# 2단계: 실행용 경량 이미지
FROM openjdk:11-jdk-slim
WORKDIR /app

# ✅ scp 명령어를 위한 openssh-client 설치 추가
RUN apt-get update && apt-get install -y openssh-client

# 빌드된 JAR 복사
COPY --from=build /app/data/api/build/libs/*.jar app.jar

# 업로드 디렉토리 생성 (권한 포함)
RUN mkdir -p /tmp/uploads && chmod 777 /tmp/uploads

# ✅ scripts 복사 (scp + ssh key)
COPY data/api/src/main/resources/scripts/scp_transfer.sh /app/scripts/scp_transfer.sh
COPY data/api/src/main/resources/scripts/id_rsa /app/scripts/id_rsa
RUN chmod +x /app/scripts/scp_transfer.sh && chmod 600 /app/scripts/id_rsa

ENTRYPOINT ["java", "-jar", "app.jar"]
