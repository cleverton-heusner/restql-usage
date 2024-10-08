package com.cleverton.restql_usage.controller;

import com.cleverton.restql_usage.model.Post;
import io.github.cleverton.heusner.selector.FieldsSelector;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PostController {

    @Autowired
    private FieldsSelector fieldsSelector;

    @GetMapping("/post/fields-selection-with-filter")
    public ResponseEntity<Post> getPostWithFieldsSelectedByFilter(
            @RequestParam(name = "fields", required = false) final String fields
    ) {
        return ResponseEntity.ok(Instancio.create(Post.class));
    }

    @GetMapping("/post/manual-fields-selection")
    public ResponseEntity<Map<String, Object>> getPostWithFieldsSelectedManually(
            @RequestParam(name = "fields", required = false) final String fields
    ) {
        return ResponseEntity.ok(fieldsSelector.from(Instancio.create(Post.class)).select(fields));
    }
}