package com.springboot.sion.blog.controller;

import com.springboot.sion.blog.dto.PostDto;
import com.springboot.sion.blog.dto.PostResponse;
import com.springboot.sion.blog.service.PostService;
import com.springboot.sion.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    //injecting an interface not the class, for loose coupling
    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Create blog post REST API
     * @param postDto
     * @return
     *
     * The annotation @Valid enables java bean validation for the api request
     * The annotation @PreAuthorize only allows users with the specified role to access this endpoint
     *
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    /**
     * Get all posts REST API
     * @return
     */
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {

        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    /**
     * Get post by specific id
     * @param id
     * @return ResponseEntity<PostDto>
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    /**
     * Request body gets json from the body to turn into java object
     * @param postDto
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id) {
        PostDto postResponse = postService.updatePost(postDto, id);

        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    /**
     * Delete post by id
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id) {
        postService.deletePostById(id);

        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }
}
