package com.otabek.blog.service;

import com.otabek.blog.dto.CategoryDTO;
import com.otabek.blog.dto.request.PostRequestDTO;
import com.otabek.blog.dto.response.PostResponseDTO;
import com.otabek.blog.entity.Category;
import com.otabek.blog.entity.Post;
import com.otabek.blog.entity.User;
import com.otabek.blog.exception.ResourceNotFoundException;
import com.otabek.blog.repository.CategoryRepository;
import com.otabek.blog.repository.PostRepository;
import com.otabek.blog.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    @Transactional
    public PostResponseDTO createPost(
        PostRequestDTO request,
        List<MultipartFile> images
    ) {
        User author = userRepository
            .findById(request.getAuthorId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "User",
                    "id",
                    request.getAuthorId()
                )
            );
        List<Category> categories = categoryRepository.findAllById(
            request.getCategoryIds()
        );
        
        
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(author);
        post.setCategories(categories);
        post.setTags(request.getTags());
        
        post.setSlug(generateSlug(request.getTitle()));
    
        if (images != null && !images.isEmpty()) {
            List<String> urls = new ArrayList<>();
            images.forEach(image -> {
                String imageUrl = imageService.uploadImage(image);
                urls.add(imageUrl);
            });
            post.setImageUrls(urls);
        }

        Post savedPost = postRepository.save(post);
        return mapToResponseDto(savedPost);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
            .stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToResponseDto(post);
    }

    @Transactional(readOnly = true)
    public PostResponseDTO getPostBySlug(String slug) {
        Post post = postRepository
            .findBySlug(slug)
            .orElseThrow(() ->
                new RuntimeException("Post not found with slug: " + slug)
            );
        return mapToResponseDto(post);
    }

    @Transactional
    public PostResponseDTO updatePost(
        Long id,
        PostRequestDTO request,
        List<MultipartFile> images
    ) {
        Post post = postRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    
        if (request.getTitle() != null) {
            post.setTitle(request.getTitle());
            post.setSlug(generateSlug(request.getTitle()));
        }
        if (request.getContent() != null) post.setContent(request.getContent());
        if (request.getTags() != null) post.setTags(request.getTags());
    
        if (
            request.getCategoryIds() != null &&
            !request.getCategoryIds().isEmpty()
        ) {
            List<Category> categories = categoryRepository.findAllById(
                request.getCategoryIds()
            );
            post.setCategories(categories);
        }
    
        List<String> finalImageUrls = new ArrayList<>();
        
        if (request.getExistingImageUrls() != null) {
            finalImageUrls.addAll(request.getExistingImageUrls());
        } else {
            if (post.getImageUrls() != null) {
                finalImageUrls.addAll(post.getImageUrls());
            }
        }
    
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String imageUrl = imageService.uploadImage(image);
                    finalImageUrls.add(imageUrl);
                }
            }
        }
        
        post.setImageUrls(finalImageUrls);
    
        Post updatedPost = postRepository.save(post);
        return mapToResponseDto(updatedPost);
    }


    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    private String generateSlug(String title) {
        String slug = title.toLowerCase()
        .replaceAll("[^a-z0-9\\-]", "-")
        .replaceAll("-+", "-");
        Post post2 = postRepository.findBySlug(slug).orElse(null);
        if (post2 == null)
        return title
            .toLowerCase()
            .replaceAll("[^a-z0-9\\-]", "-")
            .replaceAll("-+", "-");
        return title.toLowerCase()
            .replaceAll("[^a-z0-9\\-]", "-")
            .replaceAll("-+", "-")+UUID.randomUUID().toString().substring(0,5);
    }

    public PostResponseDTO mapToResponseDto(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setSlug(post.getSlug());
        dto.setContent(post.getContent());
        dto.setImageUrls(post.getImageUrls());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdateAt());
        dto.setTags(post.getTags());
        System.out.println("Mapping post to response DTO");
        dto.setAuthorUsername(post.getAuthor().getUsername());

        if (post.getCategories() != null) {
            List<CategoryDTO> catDtos = post
                .getCategories()
                .stream()
                .map(cat -> {
                    CategoryDTO catDto = new CategoryDTO();
                    catDto.setId(cat.getId());
                    catDto.setName(cat.getName());
                    catDto.setDescription(cat.getDescription());
                    return catDto;
                })
                .collect(Collectors.toList());
            dto.setCategories(catDtos);
        }
        return dto;
    }
}