#!/bin/sh

# Make sure only root can run our script
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

readonly KVAL=4
# Start floodlight controller
echo "Starting floodlight Controller as a background process"
cd ~/floodlight
sudo java -jar target/floodlight.jar >~/floodlight/floodlight.log 2>&1 &

echo "Deploying fat-tree topology"
sudo mn -c
cd ~/group6/topogen
sudo python ./fat_tree_topology.py $KVAL
