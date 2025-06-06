# 1단계: Gradle 빌드
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# build.gradle, settings.gradle, src 복사
COPY build.gradle settings.gradle ./
COPY src ./src

# JAR 빌드
RUN gradle clean bootJar --no-daemon

# 2단계: 실행용 경량 이미지
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드된 JAR 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 업로드 디렉토리 생성 (권한 포함)
RUN mkdir -p /tmp/uploads && chmod 777 /tmp/uploads

ENTRYPOINT ["java", "-jar", "app.jar"]
