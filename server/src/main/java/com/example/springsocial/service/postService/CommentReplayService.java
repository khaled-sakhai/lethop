package com.example.springsocial.service.postService;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Reply;
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


     public Comment addCommentForPost(Long postId, Comment comment) {
        // Fetch the post by ID and set it for the comment
        Post post = postRepo.findPostByIdAndIsArchivedFalseAndIsPublic(postId,true)
        .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        comment.setPost(post);

        return commentRepo.save(comment);
    }

  public Reply addReplyToComment(Long commentId, Reply reply) {
        // Fetch the comment by ID and set it for the reply
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        reply.setComment(comment);
        return replyRepo.save(reply);
    }

}
