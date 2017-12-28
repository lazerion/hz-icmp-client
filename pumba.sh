#!/usr/bin/env bash

pumba netem --tc-image gaiadocker/iproute2 --duration 1m delay --time 5000 re2:.*hazelcast[_]{1}[1]{1}.*
pumba netem --tc-image gaiadocker/iproute2 --duration 30s delay --time 5000 re2:.*hazelcast[_]{1}[2]{1}.*
