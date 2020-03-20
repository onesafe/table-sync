## 配置文件application.properties
```bash
# 写入的数据库
spring.datasource.username=root  
spring.datasource.password=empower  
spring.datasource.url=jdbc:mysql://172.27.137.230:3306/cas?useUnicode=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# 读取的数据库配置
canal.url=172.27.137.221
canal.port=41111
srcdb.name=cas-test  # 从这个数据库读取binlog
srcdb.table=user   # 从这张表读取binlog
destdb.name=cas   # 写入的数据库的库名称
```

## 通过脚本启动服务
```bash
# 先修改配置文件

# 打包
./scripts/build.sh

# 打包完了scripts目录下面有个table-sync.tar.gz文件，可以把它拷贝到我们要执行的机器上面

# 解压table-sync.tar.gz后执行
./bin/start.sh 
```

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