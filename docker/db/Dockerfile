#docker/db/Dockerfile
FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=login_db
ENV MYSQL_USER=user
ENV MYSQL_PASSWORD=password

# 문자셋 설정
ENV LANG=C.UTF-8

# 초기화 스크립트 복사
COPY ../../data/db/init.sql /docker-entrypoint-initdb.d/

EXPOSE 3306

CMD ["mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci", "--default-authentication-plugin=mysql_native_password"] 