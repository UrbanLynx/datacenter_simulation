#!/bin/sh

# RUN THIS ON HOST OS of mininet, all mininet hosts need not be running varys.
# Make sure only root can run our script
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

# Building And Starting varys
echo "BUILDING AND STARTING VARYS"
cd ~/group6/varys-master
#./sbt/sbt package
#./sbt/sbt publish-local
export SCALA_HOME=/usr/bin/scala

./bin/start-all.sh
