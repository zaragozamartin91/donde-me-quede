#!/bin/bash

docker run -it --rm --link dmq-db postgres psql -h dmq-db -U dondemequede -p 5432 dmq
