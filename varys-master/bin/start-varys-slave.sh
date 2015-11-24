#!/usr/bin/env bash

# Start all varys daemons.
# Starts the master on this node.
# Starts a slave on each node specified in conf/slaves

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

# Load the Varys configuration
. "$bin/varys-config.sh"

#export VARYS_MASTER_IP=$(hostname -I | cut -d' ' -f1)

#if [ "$VARYS_MASTER_IP" = "" ]; then
  VARYS_MASTER_IP=$1
echo $VARYS_MASTER_IP

export VARYS_LOCAL_IP=$(hostname -I | cut -d' ' -f1)
echo $VARYS_LOCAL_IP
# Start Master
#if [ $1 = "true" ]; then
#  "$bin"/start-master.sh

# Start Slaves
"$bin"/start-slaves.sh
