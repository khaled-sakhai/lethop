package com.example.springsocial.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.example.springsocial.util.PathConstants;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.dto.post.PostDto;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.service.postService.SearchService;
import com.example.springsocial.validator.validators.ValidPostSortBy;

@RestController

@AllArgsConstructor
public class SearchController {
 

    private final SearchService searchService;

    @GetMapping(PathConstants.SEARCH_ENDPOINT)
    public ResponseEntity<List<PostDto>>  searchPosts(
    @RequestParam String searchText, 
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(defaultValue = "desc") String sortDirection) {
    Page<Post>  postsPage= searchService.searchPosts(searchText, page, size, sortBy, sortDirection);
        if (postsPage.isEmpty()) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
       // Get the content (posts) from the Page object
       List<PostDto> postDtos = postsPage.getContent().stream()
       .map(PostDto::new)
       .collect(Collectors.toList());
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }


}
