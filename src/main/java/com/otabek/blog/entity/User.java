package com.otabek.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String bio;
    private String profileImageUrl;

    @ElementCollection
    @CollectionTable(name = "user_social_links", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "link_name",nullable = true)
    @Column(name = "link_url",nullable = true)
    private Map<String , String> socialLinks = new HashMap<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<Post> posts = new ArrayList<>();
}