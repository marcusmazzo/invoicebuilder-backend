package com.invoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication(scanBasePackages = "com")
public class InvoiceBuilderApplication {


    public static void main(String[] args) {

        SpringApplication.run(InvoiceBuilderApplication.class, args);
    }
}
