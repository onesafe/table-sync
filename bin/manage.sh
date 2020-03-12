#!/bin/bash

ADATE=`date +%Y%m%d%H%M%S`
APP_NAME=table-sync

APP_HOME=`pwd`
dirname $0|grep "^/" >/dev/null
if [ $? -eq 0 ];then
   APP_HOME=`dirname $0`
else
    dirname $0|grep "^\." >/dev/null
    retval=$?
    if [ $retval -eq 0 ];then
        APP_HOME=`dirname $0|sed "s#^.#$APP_HOME#"`
    else
        APP_HOME=`dirname $0|sed "s#^#$APP_HOME/#"`
    fi
fi

if [ ! -d "$APP_HOME/logs" ];then
  mkdir $APP_HOME/logs
fi

if [ ! -d "$APP_HOME/gc-logs" ];then
  mkdir $APP_HOME/gc-logs
fi

GC_LOG_PATH=$APP_HOME/gc-logs/gc-$APP_NAME-$ADATE.log

#JVM参数
JVM_OPTS="-Dname=$APP_NAME -Duser.timezone=Asia/Shanghai -Xms4096M -Xmx4096M -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps -Xloggc:$GC_LOG_PATH -XX:+PrintGCDetails -XX:NewRatio=2 -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -Xss512k"

JAR_FILE=$APP_HOME/lib/$APP_NAME.jar
pid=0
start(){
  checkpid
  if [ ! -n "$pid" ]; then
    nohup java -jar $JVM_OPTS  $JAR_FILE > /dev/null 2>&1 &
    echo "$APP_NAME is runing"
  else
      echo "$APP_NAME is runing PID: $pid"
  fi

}


status(){
   checkpid
   if [ ! -n "$pid" ]; then
     echo "$APP_NAME not runing"
   else
     echo "$APP_NAME runing PID: $pid"
   fi
}

checkpid(){
    pid=`ps -ef |grep $JAR_FILE |grep -v grep |awk '{print $2}'`
}

stop(){
    checkpid
    if [ ! -n "$pid" ]; then
     echo "$APP_NAME not runing"
    else
      echo "$APP_NAME stop..."
      kill -9 $pid
    fi
}

restart(){
    stop
    sleep 1s
    start
}

case $1 in
          start) start;;
          stop)  stop;;
          restart)  restart;;
          status)  status;;
              *)  echo "require start|stop|restart|status"  ;;
esac

