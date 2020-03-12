## 本地docker启动测试
```bash
make clean && make tablesync
pushd cicd
docker build -t table-sync .
docker run  -it --name table-sync -d table-sync

docker run  -it --name table-sync -v $cur/application-prod.properties:/app/config/application-prod.properties -d table-sync
docker logs -f table-sync
docker rm -f table-sync
popd

```