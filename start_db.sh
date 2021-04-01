#!/bin/bash

docker run --name dmq-db \
  -p 5432:5432 \
  -p 5433:5433 \
  -e POSTGRES_USER=dondemequede \
  -e POSTGRES_PASSWORD=d0nd3m3qu3d3 \
  -e POSTGRES_DB=dmq \
  -d postgres
