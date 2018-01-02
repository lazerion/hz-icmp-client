#!/usr/bin/env bash

docker-compose -f deployment-2.yaml up -d
sleep 20s
docker-compose -f deployment-2.yaml scale hazelcast=3
sleep 60s

for i in 1 2
do
  echo "Emulating ... number $i"
  pumba netem --tc-image gaiadocker/iproute2 --duration 1m delay --time 5000 re2:.*hazelcast[_]{1}[1]{1}.*
  sleep 120s # Waits 2 minutes.
done

docker logs client
docker-compose -f deployment-2.yaml down
