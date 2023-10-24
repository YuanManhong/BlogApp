package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    /*
    @RequestBody:
    This tells Spring to take the body of the web request and convert it into a CommentDTO object, which can then be used within your createComment method.
     */
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "postId") long postId,
                                                    @RequestBody CommentDTO commentDTO){
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO> getCommentByPostId(@PathVariable(value = "postId") Long postId){
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(value = "postId") Long postId,
                                                     @PathVariable(value = "commentId") Long commentId){
        CommentDTO commentDTO = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }
}
