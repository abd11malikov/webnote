package com.otabek.blog.controller;

import com.otabek.blog.dto.request.UserRequestDTO;
import com.otabek.blog.dto.response.UserResponseDTO;
import com.otabek.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestPart("data") UserRequestDTO userDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        System.out.println("Creating user...");
        UserResponseDTO createdUser = userService.create(userDto, image);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping(value = "/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable String username,
            @RequestPart("data") UserRequestDTO userDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        UserResponseDTO updatedUser = userService.updateUser(username, userDto, image);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
}