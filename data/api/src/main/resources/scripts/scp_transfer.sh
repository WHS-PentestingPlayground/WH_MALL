#!/bin/sh

SRC_FILE=$1      # 예: /tmp/tunnel.jsp
DEST_PATH=$2     # 예: /var/www/html/uploads/

scp -v -i /home/appuser/.ssh/id_rsa \
  -o StrictHostKeyChecking=no \
  -o UserKnownHostsFile=/dev/null \
  "$SRC_FILE" ctfuser@web-server:"$DEST_PATH"