FROM docker.4pd.io/base-image-openjdk8:1.0.1

MAINTAINER wangyiping@4paradigm.com

COPY release /app

ENV LOG_LEVEL info

ENV MY_MEM_LIMIT 1024

ENTRYPOINT ["/bin/bash", "/app/bin/start.sh"]
