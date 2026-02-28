package com.otabek.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@Entity
@Table(name = "categories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();
}