#!/bin/sh

SRC_FILE=$1
DEST_PATH="/usr/local/tomcat/webapps/ROOT/uploads"

scp -v -i /app/scripts/id_rsa \
  -o StrictHostKeyChecking=no \
  -o UserKnownHostsFile=/dev/null \
  "$SRC_FILE" ctfuser@web-server:"$DEST_PATH"