#docker/web/Dockerfile

# 1단계: Gradle 빌드
FROM gradle:7.6-jdk11 AS build
WORKDIR /app

# Gradle 설정
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.internal.http.connectionTimeout=180000 -Dorg.gradle.internal.http.socketTimeout=180000"
ENV GRADLE_USER_HOME=/app/.gradle

# 멀티모듈 설정 복사
COPY settings.gradle .
COPY common ./common
COPY data/web ./data/web

# Gradle 캐시 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/.gradle && chmod 777 /app/.gradle

# web 모듈 디렉토리로 이동 및 빌드
WORKDIR /app/data/web
RUN chmod +x ./gradlew
RUN ./gradlew bootWar --no-daemon --stacktrace

# 빌드 결과물 확인
RUN ls -la build/libs/

# 2단계: 톰캣 이미지로 배포
FROM tomcat:9.0.80-jdk11

# 파일 업로드 디렉토리 생성
RUN mkdir -p /var/www/html/uploads && chmod 777 /var/www/html/uploads

# 빌드된 WAR를 톰캣 ROOT로 복사
COPY --from=build /app/data/web/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
