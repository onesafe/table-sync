配置文件application.properties
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