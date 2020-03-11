## 本地docker启动测试
```bash
make clean && make patrol
pushd cicd/patrol
docker build -t test-patrol .
docker run  -it --name patrol -e MY_MEM_LIMIT=1024 -e LOG_LEVEL=info -p51880:51890 -d test-patrol
docker logs -f patrol
docker rm -f patrol
popd
```

## K8S集群启动服务
```bash
cd k8s
./replace.sh [namespace] [image]
kubectl create -f .
```