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

runCommand "rm -rf $cur/cicd/patrol/release"
runCommand "mkdir $cur/cicd/patrol/release"

runCommand "cp -r $cur/target/lib $cur/cicd/patrol/release/lib"
runCommand "cp $cur/target/patrol*.jar $cur/cicd/patrol/release/lib/"

runCommand "mkdir $cur/cicd/patrol/release/bin"
runCommand "cp $cur/src/main/resources/bin/start.sh $cur/cicd/patrol/release/bin/"
runCommand "cp $cur/src/main/resources/bin/set_memory.sh $cur/cicd/patrol/release/bin/"

runCommand "cp -r $cur/cicd/patrol/config $cur/cicd/patrol/release/"
