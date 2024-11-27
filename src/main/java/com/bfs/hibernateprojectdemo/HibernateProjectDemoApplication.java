package com.bfs.hibernateprojectdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableAspectJAutoProxy
public class HibernateProjectDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HibernateProjectDemoApplication.class, args);
    }

}
