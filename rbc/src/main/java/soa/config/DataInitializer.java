package soa.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import soa.model.Users;
import soa.repo.UsersRepo;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedUsers(UsersRepo repo) {
        return args -> {
            if (repo.findByUsername("admin") == null) {
                repo.save(new Users("Administrator", "admin", "admin123", "ADMIN"));
            }
            if (repo.findByUsername("user") == null) {
                repo.save(new Users("Normal User", "user", "user123", "USER"));
            }
        };
    }
}