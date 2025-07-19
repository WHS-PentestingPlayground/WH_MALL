#!/bin/sh

SRC_FILE=$1
DEST_PATH="/home/ctfuser/"

scp -v -i /app/scripts/id_rsa \
  -o StrictHostKeyChecking=no \
  -o UserKnownHostsFile=/dev/null \
  "$SRC_FILE" ctfuser@net_robotics_web:"$DEST_PATH"