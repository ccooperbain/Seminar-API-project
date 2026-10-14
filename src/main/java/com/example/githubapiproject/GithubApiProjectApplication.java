package com.example.githubapiproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GithubApiProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubApiProjectApplication.class, args);
    }

}
