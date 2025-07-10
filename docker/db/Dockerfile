# Oracle XE 21-slim 기반 이미지
FROM gvenzl/oracle-xe:21-slim

# 문자셋 (기본적으로 UTF-8이지만 명시 가능)
ENV LANG=C.UTF-8

# 초기화 스크립트 복사
COPY ../../data/db/init.sql /container-entrypoint-initdb.d/

# 포트 오픈 (Oracle 기본 포트)
EXPOSE 1521
