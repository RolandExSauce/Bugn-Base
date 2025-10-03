Swagger im Browser:

http://localhost:8080/swagger-ui.html

# ANLEITUNG DOCKER IMAGE BUILD/CONTAINER RUN
init.sql hat sql befehle um alle tabellen zu erstellen sie zu befüllen.

Zum erstellen des images und des containers:

1) im root-verzeichnis, wo readme und Dockerfile sind, im terminal:

   docker build -t bugnbass_db .

   (-t ist tag (name: bugnbass_db), punkt am ende = 'aus diesem verzeichnis bauen'):

das erstellt ein image anhand Dockerfile.
In Dockerfile, die Zeile:
COPY init.sql /docker-entrypoint-initdb.d/
initiiert die db und befüllt sie mit sql vom init.sql file wenn der container gestartet wird.

2) starte einen container mit funktionierender db aus diesem image. 2 möglichkeiten:

   a) container OHNE speicherplatz: bei jedem container-restart beginnt die db mit den anfänglichen daten vom init.sql:

        docker run --name bugnbass-postgres-container -p 5432:5432 -d bugnbass_db

        (--name: name vom container, -p: port binding, -d: als hintergrundprozess, zum schluss der tag(name) von dem image)

   b) container MIT speicherplatz: beim ersten mal wird die db vom init.sql befüllt, danach bestehen die daten sowie datenänderungen auf der db:

        docker run --name bugnbass-postgres-container \
          -e POSTGRES_DB=bugnbass \
          -e POSTGRES_USER=bugnbass \
          -e POSTGRES_PASSWORD=bugnbass \
          -v bugnbass_pgdata:/var/lib/postgresql/data \
          -p 5432:5432 \
          -d bugnbass_db

        Achtung: Auch wenn man den ursprünglichen container löscht und einen neuen container startet, wird der volume nicht gelöscht und die alten daten werden aus dem volume geladen. 
        Den Speicherplatz kann man löschen mit:
          docker volume rm <name vom volume>
          docker volume rm bugnbass_pgdata

3a) done! nun kann die db in bugnbass_backend im IDE integriert werden während der kontainer läuft. sb app.properties ist konfiguriert.

3b) um die postgres db innerhalb des containers über terminal zu erreichen:

docker exec -it bugnbass-postgres-container psql -U bugnbass -d bugnbass
