FROM gvenzl/oracle-free:slim

ENV LANG=C.UTF-8

USER root

RUN microdnf install -y gettext

COPY ./data/db/init.sql /tmp/init.sql
RUN mkdir -p /opt/oracle/scripts/startup \
    && envsubst < /tmp/init.sql > /opt/oracle/scripts/startup/init.sql

EXPOSE 1521

USER oracle
