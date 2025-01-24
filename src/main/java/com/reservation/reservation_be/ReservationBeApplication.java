package com.reservation.reservation_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ReservationBeApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ReservationBeApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ReservationBeApplication.class);
    }
}
