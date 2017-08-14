# DeepClue Spring Boot Starter

## Features

* [x]  统一 HTTP Log 处理
* [x]  统一异常处理 
* [x]  提供通用的 json字符串与java对象转换工具类
* [x]  统一的 Response 格式
* [x]  使 ＠Valid 生效

## Setup

Add the Spring boot starter to your project

```xml
<dependency>
    <groupId>cn.deepclue</groupId>
    <artifactId>deepclue-common-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

for gradle

```groovy
compile('cn.deepclue:deepclue-common-starter:0.0.1-SNAPSHOT')
```

## Show Case

```
@RequestMapping("/test")
public String test() {
    return "test";
}
```

#### response:

`{"errno":0,"errmsg":"success","data":"test"}`

#### log:

```
2017-07-07 15:33:01.342  INFO 20537 --- [nio-8080-exec-3] [line:  31] c.d.s.b.a.log.LogRequestInterceptor      : [2be4e1b7-fefc-44b4-abae-3ef3ba39570c]-[/test]
2017-07-07 15:33:01.365  INFO 20537 --- [nio-8080-exec-3] [line:  36] c.d.s.b.a.log.LogResponseBodyAdvice      : [2be4e1b7-fefc-44b4-abae-3ef3ba39570c]-[/test]-["{\"errno\":0,\"errmsg\":\"success\",\"data\":\"test\"}"]
```

---

```
@RequestMapping("/test/myerror")
public int myerror() {
    throw new AppException("error_message", AppErrorEnum.DEMO_NOT_EXISTS);
}
```

#### response:

```json
{
    "errno": 1001,
    "errmsg": "不存在对应的对象",
    "data": null
}
```

#### log:

```
2017-07-07 15:42:25.227  INFO 20537 --- [nio-8080-exec-5] [line:  31] c.d.s.b.a.log.LogRequestInterceptor      : [3823304a-39d4-4e12-943d-4783fba6d9cd]-[/test/myerror]
2017-07-07 15:42:25.299 ERROR 20537 --- [nio-8080-exec-5] [line:  39] c.d.s.b.a.RestExceptionHandler           : 处理http请求异常, errrno: 1001, errmsg: error_message
2017-07-07 15:42:25.319  INFO 20537 --- [nio-8080-exec-5] [line:  36] c.d.s.b.a.log.LogResponseBodyAdvice      : [3823304a-39d4-4e12-943d-4783fba6d9cd]-[/test/myerror]-[{"errno":1001,"errmsg":"不存在对应的对象","data":null}]
```
