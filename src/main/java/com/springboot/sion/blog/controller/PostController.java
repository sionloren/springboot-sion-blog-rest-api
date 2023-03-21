package com.springboot.sion.blog.controller;

import com.springboot.sion.blog.dto.PostDto;
import com.springboot.sion.blog.dto.PostResponse;
import com.springboot.sion.blog.service.PostService;
import com.springboot.sion.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(
        name = "CRUD REST APIs for Post Resource"
)
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
     * The annotation @SecurityRequirement is used to enable authentication for usage of the API in
     * the SWAGGER documentation
     * The annotation @Operation creates a custom summary/description of the endpoint that will
     * be displayed in the SWAGGER documentation
     * The annotation @ApiResponse provides a custom response code and description of the endpoint that
     * will be displayed in the SWAGGER documentation
     */
    @Operation(
            summary = "Create Post REST API",
            description = "Create Post REST API is used to save a new post into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    /**
     * Get all posts REST API
     * @return
     */
    @Operation(
            summary = "Get All Posts REST API",
            description = "Get All Posts REST API is used to get all posts from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
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
    @Operation(
            summary = "Get Post By Id REST API",
            description = "Get Post By Id REST API is used to get the specified post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
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
    @Operation(
            summary = "Update Post REST API",
            description = "Update Post By Id REST API is used to update the specified post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
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
    @Operation(
            summary = "Delete Post REST API",
            description = "Delete Post By Id REST API is used to delete the specified post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id) {
        postService.deletePostById(id);

        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }

    //Build get post by category REST API
    @Operation(
            summary = "Get Posts By Category REST API",
            description = "Get Posts By Category REST API is used to get all posts that is associated " +
                    "with the specified category from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable(name = "id") Long categoryId) {
        List<PostDto> postDtos = postService.getPostsByCategory(categoryId);

        return ResponseEntity.ok(postDtos);
    }
}
