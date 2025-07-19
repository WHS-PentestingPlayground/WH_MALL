# 1단계: Gradle 빌드
FROM gradle:7.6-jdk11 AS build
WORKDIR /app

# Gradle 설정
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.internal.http.connectionTimeout=180000 -Dorg.gradle.internal.http.socketTimeout=180000"
ENV GRADLE_USER_HOME=/app/.gradle

# 공통 모듈 복사
COPY settings.gradle .
COPY common ./common

# ✅ web 모듈 전체 복사 (Gradle wrapper 포함)
COPY data/web/ /app/data/web/

# Gradle 캐시 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/.gradle && chmod 777 /app/.gradle

# web 모듈 디렉토리로 이동
WORKDIR /app/data/web

# gradlew 실행 준비
RUN apt-get update && apt-get install -y dos2unix
RUN chmod +x ./gradlew
RUN dos2unix ./gradlew
RUN ./gradlew bootWar --no-daemon --stacktrace

# 2단계: 톰캣 이미지로 배포
FROM tomcat:9.0.80-jdk11

# ctfuser 추가 및 ssh 디렉토리 생성
RUN useradd -ms /bin/bash ctfuser
RUN mkdir -p /home/ctfuser/.ssh && chmod 700 /home/ctfuser/.ssh

# 공개키 복사 및 권한 설정
COPY common/ssh-keys/id_rsa /home/ctfuser/.ssh/id_rsa
COPY common/ssh-keys/id_rsa.pub /home/ctfuser/.ssh/authorized_keys
RUN chmod 600 /home/ctfuser/.ssh/id_rsa /home/ctfuser/.ssh/authorized_keys && \
    chown -R ctfuser:ctfuser /home/ctfuser/.ssh
# openssh-server 설치 및 sshd 디렉토리 생성
RUN apt-get update && apt-get install -y openssh-server unzip openssh-client && mkdir /var/run/sshd

# WAR 복사
COPY --from=build /app/data/web/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

# WAR 압축 해제 및 업로드 폴더 생성
RUN cd /usr/local/tomcat/webapps && \
    unzip ROOT.war -d ROOT && \
    mkdir -p /usr/local/tomcat/webapps/ROOT/uploads

# 포트 노출 (웹 + SSH)
EXPOSE 8080 22

# 톰캣 및 SSH 서버 실행 (시작 시 권한 재설정)
CMD ["/bin/bash", "-c", "chown -R 1000:1000 /usr/local/tomcat/webapps/ROOT/uploads && service ssh start && catalina.sh run"]
