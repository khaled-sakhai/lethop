package com.example.springsocial.specification;

import com.example.springsocial.entity.Features.Notification;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Reply;
import com.example.springsocial.entity.userRelated.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;

public class CommentReplaySpecification {

    public static Specification<Comment> commentByPostId(Long postId  ){
        return (root, query, criteriaBuilder) -> {
            if (postId == null || postId <= 0) {
                return null;
            }
            Join<Comment, Post> postjoin = root.join("post");
            return criteriaBuilder.equal(postjoin.get("id"),postId);
        };
    }
    public static Specification<Comment> commentByUserId(Long userId ){
        return (root, query, criteriaBuilder) -> {
            if (userId == null || userId <= 0) {
                return null;
            }
            Join<Comment, User> userJoin = root.join("user");
            return criteriaBuilder.equal(userJoin.get("id"),userId);
        };
    }

    public static Specification<Comment> commentByAnonymousPost(Boolean isAnonymous ){
        return (root, query, criteriaBuilder) -> {
            if (isAnonymous == null) {
                return null;
            }

            Join<Comment, Post> postjoin = root.join("post");
            return criteriaBuilder.equal(postjoin.get("isAnonymous"),isAnonymous);
        };
    }
    public static Specification<Comment> commentById(Long commentId ){
        return (root, query, criteriaBuilder) -> {
            if (commentId == null || commentId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), commentId);
        };
    }
    public static Specification<Reply> replayByUserId(Long userId ){
        return (root, query, criteriaBuilder) -> {
            if (userId == null || userId <= 0) {
                return null;
            }
            Join<Reply, User> userJoin = root.join("user");
            return criteriaBuilder.equal(userJoin.get("id"),userId);
        };
    }
    public static Specification<Reply> replayById(Long replyId  ){
        return (root, query, criteriaBuilder) -> {
            if (replyId == null || replyId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), replyId);
        };
    }
    public static Specification<Reply> replayByCommentId(Long commentId  ){
        return (root, query, criteriaBuilder) -> {
            if (commentId == null || commentId <= 0) {
                return null;
            }
            Join<Reply,Comment> commentJoin = root.join("comment");
            return criteriaBuilder.equal(commentJoin.get("id"),commentId);
        };
    }
}
