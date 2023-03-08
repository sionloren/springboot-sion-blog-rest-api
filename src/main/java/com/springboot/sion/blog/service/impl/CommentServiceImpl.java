package com.springboot.sion.blog.service.impl;

import com.springboot.sion.blog.dto.CommentDto;
import com.springboot.sion.blog.exception.BlogAPIException;
import com.springboot.sion.blog.exception.ResourceNotFoundException;
import com.springboot.sion.blog.model.Comment;
import com.springboot.sion.blog.model.Post;
import com.springboot.sion.blog.repository.CommentRepository;
import com.springboot.sion.blog.repository.PostRepository;
import com.springboot.sion.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        //retrieve post entity by id
        Post post = getPostByPostId(postId);

        //set post to comment entity
        comment.setPost(post);

        //save comment to DB
        Comment savedComment = commentRepository.save(comment);

        return mapToDTO(savedComment);
    }

    private Post getPostByPostId(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        //retrieve comments by post id;
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream()
                .map(comment -> mapToDTO(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        //retrieve post entity by id
        Post post = getPostByPostId(postId);

        //retrieve comment by id
        Comment comment = getCommentByCommentId(commentId);

        validatePostAndCommentPartnership(comment, post);

        return mapToDTO(comment);
    }

    private Comment getCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    }

    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {
        Post post = getPostByPostId(postId);

        Comment comment = getCommentByCommentId(commentId);

        validatePostAndCommentPartnership(comment, post);

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post = getPostByPostId(postId);

        Comment comment = getCommentByCommentId(commentId);

        validatePostAndCommentPartnership(comment, post);

        commentRepository.delete(comment);
    }

    private void validatePostAndCommentPartnership(Comment comment, Post post) {
        if(!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");
        }
    }

    //Convert DTO to entity (json to pojo)
    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());

        return comment;
    }

    //Convert entity to DTO
    private CommentDto mapToDTO (Comment comment) {
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());

        return commentDto;
    }
}
