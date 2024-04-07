package com.example.springsocial.controller.admin;

import com.example.springsocial.dto.comments.CommentRequest;
import com.example.springsocial.dto.comments.CommentResponse;
import com.example.springsocial.dto.comments.ReplyResponse;
import com.example.springsocial.dto.user.UserDto;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Reply;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.service.admin.ComRepAdmin;
import com.example.springsocial.service.postService.CommentReplayService;
import com.example.springsocial.validator.validators.ValidPostSortBy;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class CommentReplyController {

    private final ComRepAdmin comRepAdmin;
    private final CommentReplayService commentReplayService;

    @GetMapping(value = "/api/admin/comments")
    public ResponseEntity<Page<CommentResponse>> findAllComment(@RequestParam(required = false)Long userId,
                                                                @RequestParam(required = false) Long postId,
                                                                @RequestParam(required = false) Long commentId,
                                                                @RequestParam(required = false)Boolean isPostAnonymous,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                                                @RequestParam(defaultValue = "20") int size,
                                                                @RequestParam(defaultValue = "desc") String sortDirection){
        Page<Comment> usersPage = comRepAdmin.findAllComments(userId,postId,commentId,isPostAnonymous,page, size, sortBy, sortDirection);
        Page<CommentResponse> commentsDtos= usersPage.map(CommentResponse::new);
        if (commentsDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(commentsDtos, HttpStatus.OK);
    }
    @GetMapping(value = "/api/admin/replies")

    public ResponseEntity<Page<ReplyResponse>> findAllReplies(@RequestParam(required = false) Long userId,
                                                              @RequestParam(required = false) Long replyId,
                                                              @RequestParam(required = false) Long commentId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "lastModifiedDate")@ValidPostSortBy String sortBy,
                                                              @RequestParam(defaultValue = "20") int size,
                                                              @RequestParam(defaultValue = "desc") String sortDirection){
        Page<Reply> usersPage = comRepAdmin.findAllRepliess( userId, replyId, commentId,page, size, sortBy, sortDirection);
        Page<ReplyResponse> repliesDtos= usersPage.map(ReplyResponse::new);
        if (repliesDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(repliesDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/comments/deleted")

    public ResponseEntity<Page<CommentResponse>> findDeletedComments(
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                    @RequestParam(defaultValue = "20") int size,
                                    @RequestParam(defaultValue = "desc") String sortDirection){
        Page<Comment> usersPage = comRepAdmin.findDeletedComments(page, size, sortBy, sortDirection);
        Page<CommentResponse> commentsDtos= usersPage.map(CommentResponse::new);
        if (commentsDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(commentsDtos, HttpStatus.OK);
    }
    @GetMapping(value = "/api/admin/replies/deleted")

    public  ResponseEntity<Page<ReplyResponse>> findDeletReply(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "lastModifiedDate")@ValidPostSortBy String sortBy,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(defaultValue = "desc") String sortDirection){
        Page<Reply> usersPage = comRepAdmin.findDeletedReplies(page, size, sortBy, sortDirection);
        Page<ReplyResponse> repliesDtos= usersPage.map(ReplyResponse::new);
        if (repliesDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(repliesDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/comment/{commentId}")
    public ResponseEntity<Comment> findComment(@PathVariable Long commentId){
        Optional<Comment> commentOptional = comRepAdmin.findAnyComment(commentId);
        if (commentOptional.isPresent()){
            return new ResponseEntity<>(commentOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/api/admin/reply/{replyId}")
    public ResponseEntity<Reply>  findReply(@PathVariable Long replyId){
        Optional<Reply> replyOptional = comRepAdmin.findAnyReply(replyId);
        if (replyOptional.isPresent()){
            return new ResponseEntity<>(replyOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Transactional
    @DeleteMapping(value = "/api/admin/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable long commentId,@RequestParam Boolean finalDelete){
        Optional<Comment> commentOptional = comRepAdmin.findAnyComment(commentId);
        if(commentOptional.isPresent()){
            if(finalDelete){
                comRepAdmin.deleteComment(commentOptional.get());
            }
            else{
                commentReplayService.removeComment(commentOptional.get());
            }
            return ResponseEntity.ok().body("Comment removed!");
        }
        else return ResponseEntity.badRequest().body("Comment Not Removed");
    }

    @PutMapping (value = "/api/admin/comment/{commentId}")
    public ResponseEntity<String> editComment(@PathVariable long commentId, @RequestBody CommentRequest commentRequest,@RequestParam(required = false) Boolean delete){
        Optional<Comment> commentOptional = comRepAdmin.findAnyComment(commentId);
        if(commentOptional.isPresent()){
            commentOptional.get().setContent(commentRequest.getContent());
            if (delete!=null){
                commentOptional.get().setDeleted(delete);
            }
            commentReplayService.editComment(commentOptional.get());


            return ResponseEntity.ok().body("Comment updated!");
        }
        else return ResponseEntity.badRequest().body("Comment Not updated");
    }

    @Transactional
    @DeleteMapping(value = "/api/admin/reply/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable long replyId,@RequestParam Boolean finalDelete){
        Optional<Reply> replyOptional = comRepAdmin.findAnyReply(replyId);
        if(replyOptional.isPresent()){
            if(finalDelete){
                comRepAdmin.deleteReply(replyOptional.get());
            }
            else{
                commentReplayService.removeReplay(replyOptional.get());
            }
            return ResponseEntity.ok().body("Reply( removed!");
        }
        else return ResponseEntity.badRequest().body("Reply Not Removed");
    }

    @PutMapping (value = "/api/admin/reply/{replyId}")
    public ResponseEntity<String> editReply(@PathVariable long replyId, @RequestBody CommentRequest commentRequest,@RequestParam(required = false) Boolean delete){
        Optional<Reply> replyOptional = comRepAdmin.findAnyReply(replyId);
        if(replyOptional.isPresent()){
            replyOptional.get().setContent(commentRequest.getContent());
            if (delete!=null){
                replyOptional.get().setDeleted(delete);
            }
           commentReplayService.editReplay(replyOptional.get());

           return ResponseEntity.ok().body("Reply updated!");
        }
        else return ResponseEntity.badRequest().body("Reply Not updated");
    }


}
