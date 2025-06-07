#docker/web/Dockerfile

# 1단계: Gradle 빌드
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# 멀티모듈 설정 복사
COPY settings.gradle ./settings.gradle
COPY common ./common
COPY data/web .web/web

# web 모듈 디렉토리로 이동
RUN gradle :data:web:bootJar --no-daemon

# 2단계: 톰캣 이미지로 배포
FROM tomcat:9.0-jdk17

# 파일 업로드 디렉토리 생성
RUN mkdir -p /var/www/html/uploads && chmod 777 /var/www/html/uploads

# 빌드된 WAR를 톰캣 ROOT로 복사
COPY --from=build /app/web/build/libs/web-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
