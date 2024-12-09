package com.example.springsocial.controller.admin;

import com.example.springsocial.dto.post.PostDto;
import com.example.springsocial.dto.post.PostDtoAdmin;
import com.example.springsocial.dto.post.PostRequest;
import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.service.ImageService;
import com.example.springsocial.service.admin.PostAdmin;
import com.example.springsocial.service.postService.CategoryService;
import com.example.springsocial.service.postService.PostService2;
import com.example.springsocial.service.postService.TagService;
import com.example.springsocial.util.PathConstants;
import com.example.springsocial.validator.validators.ValidPostSortBy;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping(path = PathConstants.API_V1+PathConstants.ADMIN_END_POINT)
public class PostAdminController {
    private final PostAdmin postAdmin;
    private final PostService2 postService2;
    private final TagService tagService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    @GetMapping(path = "post")
    public ResponseEntity<Page<PostDto>> findAll(@RequestParam(required = false) Boolean isAnonymous,
                                 @RequestParam(required = false) Long userId,
                                 @RequestParam(required = false) Long postId,
                                 @RequestParam(required = false) String category,
                                  @RequestParam(required = false) String tag,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                  @RequestParam(defaultValue = "20") int size,
                                  @RequestParam(defaultValue = "desc") String sortDirection){

        Page<Post> posts = postAdmin.findAll(isAnonymous,userId,postId,category, tag, page, size, sortBy, sortDirection);

        Page<PostDto> postDtos= posts.map(PostDto::new);
        if (postDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @GetMapping(path = "post/deleted")
    public ResponseEntity<Page<PostDtoAdmin>> findDeleted(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "last_modified_date") @ValidPostSortBy String sortBy,
                                  @RequestParam(defaultValue = "20") int size,
                                  @RequestParam(defaultValue = "desc") String sortDirection){

        Page<Post> posts = postAdmin.findDeleted(page, size, sortBy, sortDirection);

        Page<PostDtoAdmin> postDtos= posts.map(PostDtoAdmin::new);
        if (postDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
       return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @GetMapping(path = "post/{postId}")
    public ResponseEntity<Post> findAnyPostById(@PathVariable long postId){
        Optional<Post>  post = postAdmin.findAnyPostById(postId);
        if(post.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(post.get());
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
}


    @PostMapping(path = "post/edit/{postId}")
    public ResponseEntity<String> editPost(@PathVariable() Long postId,
                                           @RequestPart("post") PostRequest postRequest,
                                           @RequestParam(required = false) MultipartFile postImage,
                                           @RequestParam(required = false) Boolean delete
                                           ) throws IOException {
        Optional<Post> postObj = postService2.findPostById(postId);
        if(postObj.isPresent()){
            Post post = postObj.get();
            post.setAnonymous(postRequest.isAnonymous());
            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());
            if (delete!=null){
                post.setDeleted(delete);
            }

            if(postRequest.isTagModifies()){
                post.removeTags();
                Set<Tag> tags= tagService.setTagsToPost(postRequest.getTags());
            }
            if(postRequest.isCategoryModifies()){
                post.getCategory().removePostFromCategoryById(postId);
                Category category = categoryService.SetPostCategory(postRequest.getCategory());
                category.addPostToCategory(post);
                post.setCategory(category);
            }
            if (postRequest.isImagesModifies()){
                imageService.removeImagesFromStorage(post);
                imageService.removeImagesFromPost(post);
            }
            postService2.updatePost(post);

            return ResponseEntity.ok("Post updated successfully");
        }
        return ResponseEntity.badRequest().body("Post was not updated successfully");
    }

    @DeleteMapping(path = "post/{postId}/delete")
    @Transactional
    public ResponseEntity<String> removePost(@PathVariable Long postId,@RequestParam boolean finalDelete,
                                             Principal principal) throws Exception{
        Optional<Post> postOpt = postAdmin.findAnyPostById(postId);
        if(postOpt.isPresent()){
            Post post = postOpt.get();
            if(!post.getPostImages().isEmpty()){
                imageService.removeImagesFromStorage(post);
                imageService.removeImagesFromPost(post);
            }
            postService2.deletePost(post);
            if(finalDelete){
                postAdmin.finalDeletePost(post);
            }
            return ResponseEntity.ok("Post removed successfully");
        }
        return ResponseEntity.badRequest().body("Post was not removed successfully, please try again");
    }


}
