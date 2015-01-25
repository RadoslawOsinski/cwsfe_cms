#!/bin/bash
#clean proceses without base images

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)