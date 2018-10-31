package com.demo.wenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication()
public class WendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WendaApplication.class, args);
	}
}
