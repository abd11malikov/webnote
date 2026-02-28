package com.otabek.blog.utils;

import com.otabek.blog.entity.Category;
import com.otabek.blog.entity.Post;
import com.otabek.blog.entity.User;
import com.otabek.blog.repository.CategoryRepository;
import com.otabek.blog.repository.PostRepository;
import com.otabek.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        // User user = new User();
        // user.setFirstName("Otabek");
        // user.setLastName("Kulmatov");
        // user.setEmail("otabek.kulmatov@gmail.com");
        // user.setUsername("kulmatov");
        // user.setPassword(passwordEncoder.encode("password"));
        // user.setSocialLinks(Map.of(
        //         "github", "https://github.com/otabekkulmatov",
        //         "linkedin", "https://www.linkedin.com/in/otabek-kulmatov/"
        // ));
        // userRepository.save(user);

        
        if (userRepository.count() > 0) {
            System.out.println("Database already seeded. Skipping...");
            return;
        }

        System.out.println("Starting Data Seeding...");

        Category tech = new Category();
        tech.setName("Technology");
        tech.setDescription("All things tech and code");

        Category life = new Category();
        life.setName("Lifestyle");
        life.setDescription("Daily routine and health");

        Category travel = new Category();
        travel.setName("Travel");
        travel.setDescription("Places I have visited");

        categoryRepository.saveAll(Arrays.asList(tech, life, travel));

        User admin = new User();
        admin.setFirstName("Otabek");
        admin.setLastName("Admin");
        admin.setUsername("admin");
        admin.setEmail("admin@blog.com");
        admin.setPassword(passwordEncoder.encode("admin123")); // Hashed password
        admin.setBio("I am the administrator of this blog.");
        admin.setProfileImageUrl("https://robocontest.uz/storage/uploads/profile/34910XL5HT1VUvBOmay8RpHQUNSEf.jpg");

        User john = new User();
        john.setFirstName("John");
        john.setLastName("Doe");
        john.setUsername("johnny");
        john.setEmail("john@test.com");
        john.setPassword(passwordEncoder.encode("password"));
        john.setBio("Just a regular writer.");
        john.setProfileImageUrl("https://pub-domain.r2.dev/4555e9f4-3558-41fd-9b07-eed72d28e874-2026-01-30");

        userRepository.saveAll(Arrays.asList(admin, john));

        Post post1 = new Post();
        post1.setTitle("Welcome to My Java Blog");
        post1.setSlug("welcome-to-my-java-blog");
        post1.setContent("This is the very first post on this amazing platform built with Spring Boot.");

        post1.setImageUrls(List.of("https://placehold.co/600x400?text=Java+Spring"));
        post1.setAuthor(admin);
        post1.setCategories(Arrays.asList(tech)); // Linked to Technology
        post1.setTags(Arrays.asList("java", "spring-boot", "coding"));

        Post post2 = new Post();
        post2.setTitle("Top 10 Travel Destinations");
        post2.setSlug("top-10-travel-destinations");
        post2.setContent("Here are the places you must visit before you die...");
        post2.setImageUrls(List.of("https://placehold.co/600x400?text=Travel"));
        post2.setAuthor(john);
        post2.setCategories(Arrays.asList(travel, life));
        post2.setTags(Arrays.asList("travel", "holiday", "fun"));

        postRepository.saveAll(Arrays.asList(post1, post2));

        System.out.println("Data Seeding Completed Successfully!");
    }
}