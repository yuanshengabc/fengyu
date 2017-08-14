# DataMaster

A data cleaning and analyzing product.

## Overview

A data cleaning and analyzing product.

## Build

### 打包

./gradlew cleaner:build
环境选择参数 -Denv=prod

### 运行cleaner

./gradlew cleaner:bootRun

or

java -jar ./cleaner/build/libs/cleaner-{version}-SNAPSHOT.jar

### 运行测试用例

单元测试

./gradlew :test


集成测试
./gradlew :cleaner:integTest


代码请确保单元测试和集成测试通过后才能提交

### Tips
快速部署：./bin/deploy-remote.sh lc16

快速导入到idea intellij: ./gradlew idea
