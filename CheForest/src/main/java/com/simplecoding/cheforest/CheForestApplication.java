package com.simplecoding.cheforest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ServletComponentScan
@EnableJpaAuditing
// TODO : JPA 사용 폴더 지정
@EnableJpaRepositories(basePackages = "com.simplecoding.cheforest.jpa")
// TODO : es(엘라스틱서치) 사용 폴더 지정
@EnableElasticsearchRepositories(basePackages = "com.simplecoding.cheforest.es")
public class CheForestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheForestApplication.class, args);
    }

}
