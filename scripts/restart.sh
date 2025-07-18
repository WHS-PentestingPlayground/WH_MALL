#!/bin/bash
while true; do
  echo "Restarting web-server-1..."
  docker-compose restart web-server-1
  sleep 1800
  
  echo "Restarting web-server-2..."
  docker-compose restart web-server-2
  sleep 1800
  
  echo "Restarting api-server-1..."
  docker-compose restart api-server-1
  sleep 3600
  
  echo "Restarting api-server-2..."
  docker-compose restart api-server-2
  sleep 3600
done