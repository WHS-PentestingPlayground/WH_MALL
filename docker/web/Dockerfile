FROM tomcat:9.0-jdk17

# 파일 업로드 디렉토리 생성
RUN mkdir -p /var/www/html/uploads && chmod 777 /var/www/html/uploads

# Gradle로 빌드된 WAR 파일을 ROOT.war로 배포
COPY build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
