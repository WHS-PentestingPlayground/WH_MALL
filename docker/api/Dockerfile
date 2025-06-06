# 1단계: Gradle 빌드
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# 의존성 캐싱을 위해 먼저 build 파일 복사
COPY build.gradle settings.gradle ./
COPY src ./src

# bootJar 빌드
RUN gradle clean bootJar --no-daemon

# 2단계: 빌드된 jar 파일만 복사해 실행
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

# 업로드 디렉토리 생성
RUN mkdir -p /tmp/uploads && chmod 777 /tmp/uploads

ENTRYPOINT ["java", "-jar", "app.jar"]
