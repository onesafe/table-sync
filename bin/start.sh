#!/bin/bash -xe
ENTRANCE="table-sync"

# 获取模块工作目录的绝对路径
CWD="`pwd`/$( dirname "${BASH_SOURCE[0]}" )/.."

if [[ "$@" == "--version" ]]; then
    cd "${CWD}" && java --version
    exit 0
fi

echo "start the $ENTRANCE server"
# 执行启动前先cd到start.sh所在目录，执行stop.sh
cd "${CWD}" && sh ./bin/stop.sh 2>/dev/null || true
 
# cd到CWD目录，也就是能相对引用./bin ./logs同级目录
cd "${CWD}" && nohup java -jar lib/$ENTRANCE.jar --spring.profiles.active=prod > /dev/null 2>&1 &

# sleep 一下，检查当前jar是否在运行
sleep 2
ps -aux | grep $ENTRANCE | grep -v grep