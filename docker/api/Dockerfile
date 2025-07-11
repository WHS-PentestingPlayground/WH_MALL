# 1단계: Gradle 빌드
FROM gradle:7.6-jdk11 AS build
WORKDIR /app

# Gradle 설정
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.internal.http.connectionTimeout=180000 -Dorg.gradle.internal.http.socketTimeout=180000"
ENV GRADLE_USER_HOME=/app/.gradle

# 공통 설정 복사
COPY settings.gradle .
COPY common ./common

# API 모듈 전체 복사
COPY data/api /app/data/api/

# Gradle 캐시 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/.gradle && chmod 777 /app/.gradle

# API 모듈 디렉토리로 이동
WORKDIR /app/data/api

# dos2unix 설치 및 gradlew 준비
RUN apt-get update && apt-get install -y dos2unix
RUN chmod +x ./gradlew
RUN dos2unix ./gradlew

RUN ./gradlew bootJar --no-daemon --stacktrace

# 2단계: 실행용 경량 이미지
FROM openjdk:11-jdk-slim
WORKDIR /app

# SSH 설치 및 sshd 준비
RUN apt-get update && \
    apt-get install -y openssh-client dos2unix openssh-server unzip wget netcat-openbsd \
    python3 python3-pip && \
    mkdir /var/run/sshd

# 애플리케이션 JAR 복사
COPY --from=build /app/data/api/build/libs/*.jar app.jar

# 업로드 디렉토리 생성 및 권한 부여
RUN mkdir -p /tmp/uploads && chmod 777 /tmp/uploads

# 스크립트 및 SSH 키 복사
COPY data/api/src/main/resources/scripts/scp_transfer.sh /app/scripts/scp_transfer.sh
COPY common/ssh-keys/id_rsa /app/scripts/id_rsa
COPY common/ssh-keys/id_rsa /root/.ssh/id_rsa
COPY common/ssh-keys/id_rsa.pub /root/.ssh/authorized_keys

# SSH 키 권한 및 dos2unix 처리
RUN dos2unix /app/scripts/id_rsa && dos2unix /root/.ssh/id_rsa && dos2unix /app/scripts/scp_transfer.sh
RUN chmod 600 /root/.ssh/id_rsa /root/.ssh/authorized_keys

# 스크립트 실행 권한 설정
RUN chmod +x /app/scripts/scp_transfer.sh && chmod 600 /app/scripts/id_rsa

# 서비스 실행: ssh 데몬 시작 후 java 앱 실행
CMD ["/bin/bash", "-c", "service ssh start && java -jar app.jar"]
