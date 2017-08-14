# app 代码结构示例
```
项目src目录下的三个目录：

　　integTest:　集成测试代码(集成测试数据准备, 测试完成回滚db)
　　main:源代码
　　test:单元测试代码(mock依赖)
```
　　
### main下的分包原则
##　java源代码

```
　common:  通用功能代码
　   　exception: 自定义异常相关类
　　　 log: http请求参数，响应值拦截打印
　　　 util:　静态工具类
　　　　　RestExceptionHandler　：统一异常处理
　dao:  数据访问层
　domain:　领域对象, pojo ( Plain Ordinary Java Object)，　
　　　　　　各层对应关系　po -> dao　 bo ->service vo->controller 
　　　po: persistant object 与数据库表完全对应的实体对象
　　　bo: bussiness object 业务对象，service层使用
　　　vo: view object 与前端接口一一对应，属性应该跟接口文档保持一致
  service: 业务逻辑层
  　　impl 业务逻辑实现
  web: 与web相关的层级，包括controller ,filter 等等
  　　controller 负责具体的业务模块流程的控制
     filter 拦截器
```

## resources 配置文件
   
```
    sql目录, 表结构schema初始化脚本
    bootstrap.yml 注册中心，远程配置服务(每个服务的配置文件都放在远端的git中), 
    logback-srping.xml 日志配置文件　spring 包装后的logback.xml配置
    file-appender.xml　logback-srping.xml包含的日志appender

```
### build.gradle　文件

```
1: maven 本地和私服的配置
2: 集成测试integTest目录的配置
3: 引入微服务基础服务的客户端的依赖
	compile('org.springframework.cloud:spring-cloud-starter-bus-amqp')//消息总线依赖
	compile('org.springframework.cloud:spring-cloud-starter-config')//远程配置
	compile('org.springframework.cloud:spring-cloud-starter-eureka')//服务注册发现
	compile('org.springframework.cloud:spring-cloud-starter-feign')//客户端负载均衡
	compile('org.springframework.cloud:spring-cloud-starter-hystrix')//服务熔断
	compile('org.springframework.cloud:spring-cloud-starter-zipkin')//分布式链路跟踪


```

### 统一处理
```
1: bootstrap.yml文件 + build.gradle依赖的引入
　　实现服务的注册发现、远程配置，分布式链路跟踪等等(可以以spring-boot-starter-xxx的方式封装到公司公用的jar包中)
2: 统一日志处理 LogRequestBodyAdvice、LogRequestInterceptor、LogResponseBodyAdvice
3: 统一异常处理 RestExceptionHandler　　
4: 参数入口校验　配置LocalValidatorFactoryBean　bean, 在需要校验参数的地方加@Valid , 如下：
 片段一：
 @RequestMapping(method = RequestMethod.POST)
 public int add(@Valid DemoAddVO vo) {
 }
 
 片段二：
public class DemoAddVO {
    @NotBlank(message = "column1 不能为空")
    private String column1;
    @NotNull(message = "column2 不能为空")
    private Double column2;
    @NotNull(message = "column3　不能为空")
    @Range(min = 1, max = 100, message = "column3 必须大于１小于100 ")
    private Double column3;
     ....
     
```