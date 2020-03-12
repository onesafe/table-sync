#!/usr/bin/env bash
dir="`pwd`"

echo "${dir}"

#env=$1
#
#config_file="application-$env.properties"

[ -z "$config_file" ]  && config_file="application.properties"

pushd "${dir}"

[ -d package ] && { rm -rf package; }

mkdir -p package/table-sync

mkdir -p package/table-sync/logs

mkdir -p package/table-sync/lib

mkdir -p package/table-sync/conf

cp -r ../bin  package/table-sync/

cp  ../src/main/resources/$config_file  package/table-sync/conf/application.properties
cp  ../src/main/resources/log4j2.xml  package/table-sync/conf/log4j2.xml

popd


pushd "${dir}/.."
mvn package  -Dmaven.test.skip=true
cp  target/table-sync-0.0.1-SNAPSHOT.jar scripts/package/table-sync/lib/table-sync.jar
popd


pushd "${dir}/package/"
tar -czvf table-sync.tar.gz ./table-sync
mv "${dir}/package/table-sync.tar.gz" "${dir}"
popd

pushd "${dir}"
rm -rf  "${dir}/package/"
popd
