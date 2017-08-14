package cn.deepclue;

import cn.deepclue.common.exception.AppErrorEnum;
import cn.deepclue.common.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DeepclueSpringBootSampleApplication {
    @Value("${foo}")
    private String foo;


    public static void main(String[] args) {
        SpringApplication.run(DeepclueSpringBootSampleApplication.class, args);
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/test/error")
    public int error() {
        return 1/0;
    }

    @RequestMapping("/test/myerror")
    public int myerror() {
        throw new AppException("error_message", AppErrorEnum.DEMO_NOT_EXISTS);
    }

    @RequestMapping("/foo")
    public String foo() {
        return foo;
    }
}
