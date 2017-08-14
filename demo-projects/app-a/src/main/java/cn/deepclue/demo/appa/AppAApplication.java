package cn.deepclue.demo.appa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AppAApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppAApplication.class, args);
	}

	@Value("${foo}")
	private String foo;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String calculation() {
		return foo;
	}

}
