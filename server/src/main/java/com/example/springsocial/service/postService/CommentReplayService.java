package com.example.springsocial.service.postService;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Reply;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.CommentRepo;
import com.example.springsocial.repository.PostRepo;
import com.example.springsocial.repository.ReplyRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.service.UserService;


@Service
public class CommentReplayService {
     @Autowired
    private UserService userService;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private ReplyRepo replyRepo;
     @Autowired
    private UserRepo userRepo;
    @Autowired 
    private PostRepo postRepo;

    @Autowired
    private PostService postService;


     public Comment addCommentForPost(Long postId, Comment comment,User user) {
        // Fetch the post by ID and set it for the comment
        Post post = postRepo.findPostByIdAndIsPublic(postId,true)
        .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        comment.setPost(post);
        user.getUserComments().add(comment);
        post.getPostComments().add(comment);
        user.updateCommentsCounter();
        post.updateCommentCount();
        return commentRepo.save(comment);
    }

    public Set<Comment> getPostComments(Long postId){
        Post post = postRepo.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post not found")); 
        return post.getPostComments();
    }

    public Comment getCommentById(Long commentId){
        Comment comment = commentRepo.findById(commentId)
        .orElseThrow(() -> new EntityNotFoundException("comment not found")); 
        return comment;
    }


  public Reply addReplyToComment(Long commentId, Reply reply,User user) {
        // Fetch the comment by ID and set it for the reply
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        reply.setComment(comment);
        user.getUserReplies().add(reply);
        user.updateRepliesCounter();
        comment.addReplayToComment(reply);
        comment.updateNumberOfReplies();
        return replyRepo.save(reply);
    }

}
