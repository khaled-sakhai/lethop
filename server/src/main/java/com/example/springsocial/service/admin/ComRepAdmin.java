package com.example.springsocial.service.admin;

import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Reply;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.*;
import com.example.springsocial.security.Token.TokenRepo;
import com.example.springsocial.specification.CommentReplaySpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ComRepAdmin {

        private final UserRepo userRepo;
        private final PostAdmin postAdmin;
        private final PostRepo postRepo;
        private final TokenRepo tokenRepo;
        private final ImageRepo imageRepo;
        private final ProfileRepo profileRepo;
        private CommentRepo commentRepo;
        private ReplyRepo replyRepo;

    public Page<Comment> findAllComments(Long userId,Long postId,Long commentId,Boolean isPostAnonymous, int pageNo,int pageSize,String sortBy,String sortDirection){
        Specification<Comment> spec= this.adminCommentSpec(userId,postId,commentId,isPostAnonymous);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        return commentRepo.findAll(spec,paging);
    }
    public Page<Reply> findAllRepliess(Long userId,Long replyId,Long commentId, int pageNo,int pageSize,String sortBy,String sortDirection){
        Specification<Reply> spec= this.adminReplySpec(userId,replyId,commentId);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        return replyRepo.findAll(spec,paging);
    }

    public Page<Comment> findDeletedComments(int pageNo,int pageSize,String sortBy,String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        return commentRepo.adminFindAllDeleted(paging);
    }
    public Page<Reply> findDeletedReplies(int pageNo,int pageSize,String sortBy,String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        return replyRepo.adminFindAllDeleted(paging);
    }

    public Optional<Comment> findAnyComment(long id){
        return commentRepo.adminFindById(id);
    }

    public Optional<Reply> findAnyReply(long id){
        return replyRepo.adminFindById(id);
    }
    @Transactional
    public void deleteComment(Comment comment){
        User user = comment.getUser();
        Post post= comment.getPost();
        comment.setPost(null);
        comment.setUser(null);
        comment.getReplies().forEach(r->this.deleteReply(r));
        commentRepo.adminDeleteById(comment.getId());
    }

    public void deleteReply(Reply reply){
        User user = reply.getUser();
        Comment comment= reply.getComment();
        reply.setUser(null);
        reply.setComment(null);
        replyRepo.adminDeleteById(reply.getId());
    }

    private Specification<Comment> adminCommentSpec(Long userId,Long postId,Long commentId,Boolean isPostAnonymous){
        return Specification.where(
                CommentReplaySpecification.commentByUserId(userId)
                        .and(CommentReplaySpecification.commentByPostId(postId))
                        .and(CommentReplaySpecification.commentById(commentId))
                        .and(CommentReplaySpecification.commentByAnonymousPost(isPostAnonymous))
        );
    }

    private Specification<Reply> adminReplySpec(Long userId,Long replyId,Long commentId){
        return Specification.where(
                CommentReplaySpecification.replayByUserId(userId)
                        .and(CommentReplaySpecification.replayById(replyId))
                        .and(CommentReplaySpecification.replayByCommentId(commentId))
        );
    }


}
