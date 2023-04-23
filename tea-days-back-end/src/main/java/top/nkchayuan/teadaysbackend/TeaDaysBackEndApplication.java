package top.nkchayuan.teadaysbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("top.nkchayuan.teadaysbackend.mapper")
@SpringBootApplication
public class TeaDaysBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeaDaysBackEndApplication.class, args);
	}
}
