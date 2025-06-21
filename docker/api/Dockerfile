# 1단계: Gradle 빌드
FROM gradle:7.6-jdk11 AS build
WORKDIR /app

# Gradle 멀티모듈 프로젝트 구조 복사
COPY settings.gradle .
COPY common ./common
COPY data/api/ ./ 

# Gradle 설정
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.internal.http.connectionTimeout=180000 -Dorg.gradle.internal.http.socketTimeout=180000"
ENV GRADLE_USER_HOME=/app/.gradle

# Gradle 래퍼 권한 설정 및 빌드
RUN chmod +x ./gradlew
WORKDIR /app

# Gradle 캐시 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/.gradle && chmod 777 /app/.gradle

# 빌드 실행
RUN ./gradlew bootJar --no-daemon --stacktrace

# 빌드 결과물 확인
RUN ls -la build/libs/

# 2단계: 실행용 경량 이미지
FROM openjdk:11-jdk-slim
WORKDIR /app

# SSH 설치
RUN apt-get update && apt-get install -y openssh-client dos2unix openssh-server unzip && mkdir /var/run/sshd

# 애플리케이션 JAR 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 업로드 디렉토리 생성
RUN mkdir -p /tmp/uploads && chmod 777 /tmp/uploads

# 스크립트 및 키 복사 → 먼저 복사한 다음 권한 설정
COPY data/api/src/main/resources/scripts/scp_transfer.sh /app/scripts/scp_transfer.sh

COPY common/ssh-keys/id_rsa /app/scripts/id_rsa
COPY common/ssh-keys/id_rsa /root/.ssh/id_rsa
COPY common/ssh-keys/id_rsa.pub /root/.ssh/authorized_keys
RUN dos2unix /app/scripts/id_rsa && dos2unix /root/.ssh/id_rsa
RUN chmod 600 /root/.ssh/id_rsa /root/.ssh/authorized_keys

# 권한 설정 (복사 후!)
RUN chmod +x /app/scripts/scp_transfer.sh && chmod 600 /app/scripts/id_rsa

# 서비스 실행
CMD ["/bin/bash", "-c", "service ssh start && java -jar app.jar"]

