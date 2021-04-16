#!/bin/bash

docker run -it --rm --link dmq-db -e PGPASSWORD=d0nd3m3qu3d3 postgres psql -h dmq-db -U dondemequede -p 5432 dmq
