package com.briar.server;

import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.model.domainmodelclasses.UserContact;
import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MappedTypes({User.class, UserContact.class})
@MapperScan("com.briar.server.mapper")
@ComponentScan(basePackages = {"com.briar.server.config", "com.briar.server.resources"})
@SpringBootApplication
public class WebserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebserverApplication.class, args);
	}
}
