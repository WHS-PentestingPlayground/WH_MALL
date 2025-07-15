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

# tomcat 사용자 생성 (UID 1000)
RUN useradd -u 1000 -ms /bin/bash tomcat

# 업로드 디렉토리 생성 및 권한 최소화 (tomcat만 접근)
RUN mkdir -p /tmp/uploads && \
    chown tomcat:tomcat /tmp/uploads && \
    chmod 700 /tmp/uploads

# 스크립트 디렉토리 생성 및 복사, 권한 최소화
RUN mkdir -p /app/scripts
COPY data/api/src/main/resources/scripts/scp_transfer.sh /app/scripts/scp_transfer.sh
RUN chown tomcat:tomcat /app/scripts/scp_transfer.sh && \
    chmod 700 /app/scripts/scp_transfer.sh

# SSH 키를 tomcat 홈에 복사, 권한 최소화
RUN mkdir -p /home/tomcat/.ssh
COPY common/ssh-keys/id_rsa /home/tomcat/.ssh/id_rsa
COPY common/ssh-keys/id_rsa.pub /home/tomcat/.ssh/authorized_keys
RUN dos2unix /home/tomcat/.ssh/id_rsa && \
    dos2unix /home/tomcat/.ssh/authorized_keys
RUN chown -R tomcat:tomcat /home/tomcat/.ssh && \
    chmod 700 /home/tomcat/.ssh && \
    chmod 600 /home/tomcat/.ssh/id_rsa && \
    chmod 600 /home/tomcat/.ssh/authorized_keys

# jar 파일 권한 최소화
RUN chown tomcat:tomcat /app/app.jar && chmod 700 /app/app.jar

# 불필요한 setuid/setgid bit 제거, 홈 디렉토리 외 접근 제한(예시)
RUN chmod -R go-rwx /home/tomcat

# 서비스 실행을 tomcat 계정으로 전환
USER tomcat

# 포트 노출
EXPOSE 8081 22

# 서비스 실행: tomcat 계정으로만 동작
CMD ["/bin/bash", "-c", "service ssh start && java -jar /app/app.jar"]
