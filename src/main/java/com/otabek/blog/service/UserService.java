package com.otabek.blog.service;

import com.otabek.blog.dto.CategoryDTO;
import com.otabek.blog.dto.request.UserRequestDTO;
import com.otabek.blog.dto.response.PostResponseDTO;
import com.otabek.blog.dto.response.UserResponseDTO;
import com.otabek.blog.entity.Post;
import com.otabek.blog.entity.User;
import com.otabek.blog.exception.ResourceNotFoundException;
import com.otabek.blog.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final PostService postService;

    @Transactional
    public UserResponseDTO create(
        UserRequestDTO request,
        MultipartFile profileImage
    ) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setBio(request.getBio());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = imageService.uploadImage(profileImage);
            user.setProfileImageUrl(imageUrl);
        }

        User savedUser = userRepository.save(user);
        return mapToResponseDto(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository
            .findAll()
            .stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToResponseDto(user);
    }

    @Transactional
    public UserResponseDTO updateUser(
        String username,
        UserRequestDTO request,
        MultipartFile profileImage
    ) {
        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() ->
                new ResourceNotFoundException("User", "username", username)
            );

        if (request.getFirstName() != null) user.setFirstName(
            request.getFirstName()
        );
        if (request.getLastName() != null) user.setLastName(
            request.getLastName()
        );
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getSocialMediaLinks() != null) user.setSocialLinks(
            request.getSocialMediaLinks()
        );

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = imageService.uploadImage(profileImage);
            user.setProfileImageUrl(imageUrl);
        }

        User updatedUser = userRepository.save(user);
        return mapToResponseDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDTO getByUsername(String username) {
        return mapToResponseDto(
            userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                    new RuntimeException("User not found: " + username)
                )
        );
    }

    private UserResponseDTO mapToResponseDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();
    
        if (user.getSocialLinks() != null) {
            dto.setSocialMediaLinks(new HashMap<>(user.getSocialLinks()));
        }
    
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setProfileImageUrl(user.getProfileImageUrl());
    
        List<PostResponseDTO> posts = new ArrayList<>();
        List<Post> userPosts = user.getPosts(); 
        
        if (userPosts != null) {
            for (Post post : userPosts) {
                PostResponseDTO postResponseDTO = new PostResponseDTO();
                postResponseDTO.setId(post.getId());
                postResponseDTO.setTitle(post.getTitle());
                postResponseDTO.setSlug(post.getSlug());
                postResponseDTO.setContent(post.getContent());
                postResponseDTO.setImageUrls(post.getImageUrls());
                postResponseDTO.setCreatedAt(post.getCreatedAt());
                postResponseDTO.setUpdatedAt(post.getUpdateAt());
                postResponseDTO.setTags(post.getTags());
                postResponseDTO.setAuthorUsername(user.getUsername());
                
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
                    postResponseDTO.setCategories(catDtos);
                }
    
                posts.add(postResponseDTO);
            }
        }
    
        posts.sort(Comparator.comparing(PostResponseDTO::getCreatedAt).reversed());
    
        dto.setPosts(posts);
        return dto;
    }

    @Transactional
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() ->
                new RuntimeException("User not found: " + username)
            );
        System.out.println(user.getFirstName());
        return mapToResponseDto(user);
    }
}
