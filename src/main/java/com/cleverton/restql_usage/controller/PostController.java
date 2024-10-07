package com.cleverton.restql_usage.controller;

import com.cleverton.restql_usage.model.Post;
import org.instancio.Instancio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @GetMapping("/post")
    public ResponseEntity<Post> getPost(@RequestParam(name = "fields",
            required = false) final String fields) {
        return ResponseEntity.ok(Instancio.create(Post.class));
    }
}