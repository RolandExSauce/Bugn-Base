FROM postgres:latest

ENV POSTGRES_USER=bugnbass
ENV POSTGRES_DB=bugnbass
ENV POSTGRES_PASSWORD=bugnbass

COPY init.sql /docker-entrypoint-initdb.d/
