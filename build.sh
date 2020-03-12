#!/usr/bin/env bash

log() {
  echo "[COMMAND] ${@:1}"
}

runCommand() {
  cmd=${@:1}
  log $cmd
  $cmd
  code=$?
  if [ $code != 0 ] ; then
    echo "$cmd finished with code $code, terminating..."
    exit $code
  fi
}

cur=$(pwd)

runCommand "rm -rf $cur/cicd/release"
runCommand "mkdir $cur/cicd/release"

runCommand "cp $cur/target/table-sync*.jar $cur/cicd/release/"

runCommand "mkdir $cur/cicd/release/bin"
runCommand "cp $cur/src/main/resources/bin/start.sh $cur/cicd/release/bin/"
runCommand "cp $cur/src/main/resources/bin/set_memory.sh $cur/cicd/release/bin/"

runCommand "cp -r $cur/cicd/config $cur/cicd/release/"
