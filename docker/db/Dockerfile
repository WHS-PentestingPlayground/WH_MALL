FROM gvenzl/oracle-xe:21

ENV LANG=C.UTF-8

ARG ORACLE_USERNAME
ARG ORACLE_PASSWORD

ENV ORACLE_USERNAME=${ORACLE_USERNAME}
ENV ORACLE_PASSWORD=${ORACLE_PASSWORD}

USER root

RUN mkdir -p /var/cache/yum/metadata

RUN mkdir -p /opt/oracle/oradata && \
    chown -R 54321:54321 /opt/oracle/oradata

RUN microdnf install -y gettext

COPY ./docker/db/init.sql.template /tmp/init.sql.template

RUN mkdir -p /opt/oracle/scripts/startup && \
    envsubst < /tmp/init.sql.template > /opt/oracle/scripts/startup/init.sql

EXPOSE 1521

USER oracle
