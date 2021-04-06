#!/bin/bash

db_host=${host:-dmq-db}
ch_file=${changeLogFile:-changelog-master.xml}
db_user=${username:-dondemequede}
db_pass=${password:-d0nd3m3qu3d3}
db_name=${database:-dmq}
lq_command=${command:-generateChangeLog}

docker run --rm -v $(pwd)/src/main/resources/db/changelog:/liquibase/changelog --link dmq-db liquibase/liquibase:4.2 \
  --changeLogFile=$ch_file \
  --username=$db_user \
  --password=$db_pass \
  --url=jdbc:postgresql://$db_host:5432/$db_name \
  --driver=org.postgresql.Driver \
  $lq_command

# java -jar liquibase.jar --changeLogFile=/liquibase/changelog/pepe.xml --username=dondemequede --password=d0nd3m3qu3d3 --url=jdbc:postgresql://dmq-db:5432/dmq
