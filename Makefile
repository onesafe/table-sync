.PHONY: patrol clean

tablesync:
	mvn clean package -DskipTests
	bash build.sh

clean:
	mvn clean
	rm -rf target/*
	rm -rf cicd/release
