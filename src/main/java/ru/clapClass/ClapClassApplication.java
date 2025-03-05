package ru.clapClass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.clapClass.utils.MemoryStats;

import java.util.TimeZone;

@SpringBootApplication
public class ClapClassApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3:00"));
        SpringApplication.run(ClapClassApplication.class, args);
        MemoryStats.log();
    }
}




