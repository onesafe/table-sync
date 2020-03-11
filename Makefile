.PHONY: patrol clean

patrol:
	mvn clean package -DskipTests
	bash build.sh

clean:
	mvn clean
	rm -rf target/*
	rm -rf cicd/patrol/release
