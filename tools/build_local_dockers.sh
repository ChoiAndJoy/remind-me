#!/bin/bash -e

usage() {
  echo " "
  echo "Usage: $(basename $0) <service|db-migration|all>"
  echo " "
  exit 1
}

if [ -z "${1}" ]; then
  usage
fi

build_service() {
  sbt -batch "project task-manager" "assembly"
  (cd remind-me && docker_build_repo_tag.sh develop)
}

build_db_migration() {
  (cd retail-db-migration && docker_build_repo_tag.sh develop)
}

PROJ="$1"

case "$PROJ" in
  "service")
    build_service
    ;;
  "db-migration")
    build_db_migration
    ;;
  "all")
    build_service
    build_db_migration
    ;;
  *)
    usage
    ;;
esac