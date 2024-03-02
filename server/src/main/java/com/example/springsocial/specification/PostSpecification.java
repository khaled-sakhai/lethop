package com.example.springsocial.specification;

import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class PostSpecification {
    public static Specification<Post> isAnonymous(boolean isAnonymous){
    return(
        Root<Post> root,
        CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder builder) ->{
        return builder.equal(root.get("isAnonymous"),isAnonymous);
          };
    }

    public static Specification<Post> savedByUser(Long userId){
        return(
            Root<Post> root,
            CriteriaQuery<?> criteriaQuery,
            CriteriaBuilder builder) ->{
                Join<Post, User> savedByUsersJoin = root.join("savedByUsers");
                return builder.equal(savedByUsersJoin.get("id"), userId);
        };
    }

    // to be used only for admin// users should see deleted posts.
    public static Specification<Post> isDeleted(boolean isDeleted){
        return(
                Root<Post> root,
                CriteriaQuery<?> criteriaQuery,
                CriteriaBuilder builder) ->{
            return builder.equal(root.get("deleted"),isDeleted);
        };
    }
 
    public static Specification<Post> postWithCategory(String category,String defaultCategory){
        return(
                Root<Post> root,
                CriteriaQuery<?> criteriaQuery,
                CriteriaBuilder builder) ->{
            if(  category==null||category.isBlank()){
                return null;
            }
            Join<Post, Category> categoryjoin = root.join("category");

            // Check if the provided category exists
            Predicate categoryPredicate = builder.equal(builder.lower(categoryjoin.get("category")), category.toLowerCase());

            // If the provided category does not exist, apply the filter for the default category
            if (criteriaQuery.where(categoryPredicate).getRestriction() == null) {
                categoryPredicate = builder.equal(builder.lower(categoryjoin.get("category")), defaultCategory.toLowerCase());
            }

            return categoryPredicate;
        };
    }

    public static Specification<Post> postWithTag(String tag,String defaultTagName){
        return(
                Root<Post> root,
                CriteriaQuery<?> criteriaQuery,
                CriteriaBuilder builder) ->{
            if(tag==null || tag.isBlank()){
                return null;
            }
            // Join with the Tag entity
            Join<Post, Tag> tagJoin = root.join("listTags", JoinType.INNER);

            // Check if the provided tag exists
            Predicate tagPredicate = builder.equal(builder.lower(tagJoin.get("tagName")), tag.toLowerCase());

            // If the provided tag does not exist, apply the filter for the default tag
            if (criteriaQuery.where(tagPredicate).getRestriction() == null) {
                tagPredicate = builder.equal(builder.lower(tagJoin.get("tagName")), defaultTagName.toLowerCase());
            }

            return tagPredicate;
        };
    }

    public static Specification<Post> postByUserId(Long userId){
        return(
                Root<Post> root,
                CriteriaQuery<?> criteriaQuery,
                CriteriaBuilder builder) ->{
            if(  userId<=0){
                return null;
            }
            Join<Post, User> userJoin = root.join("user");
            return builder.equal(userJoin.get("id"),userId);
        };
    }

    public static Specification<Post> postWithTitle(String title){
        return(Root<Post> root,
        CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder builder)->{
            if(title==null || title.isBlank()){
                return null;
            }

            return builder.like(root.get("title"), "%"+title+"%");
        };
    }

    public static Specification<Post> hasLikesInRange(int minLikes, int maxLikes) {
// how to use this?
//    int minLikes = 10; // Replace with desired minimum likes
//    int maxLikes = Integer.MAX_VALUE; // Replace with desired maximum likes (Integer.MAX_VALUE for unlimited)
//    Sort sort = Sort.by(Sort.Direction.ASC, "likesCount");
//    List<Post> posts = postRepository.findAll(hasLikesInRange(minLikes, maxLikes), sort);
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("likesCount"), minLikes, maxLikes);
        };
    }
    public static Specification<Post> hasSavesInRange(int minSaves, int maxSaves) {
// how to use this?
//    int minSaves = 10; // Replace with desired minimum likes
//    int maxSaves = Integer.MAX_VALUE; // Replace with desired maximum likes (Integer.MAX_VALUE for unlimited)
//    Sort sort = Sort.by(Sort.Direction.ASC, "savesCount");
//    List<Post> posts = postRepository.findAll(hasSavesInRange(minLikes, maxLikes), sort);
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("savesCount"), minSaves, maxSaves);
        };
    }

    public static Specification<Post> hasCommentsInRange(int minComments, int maxComments) {
        // how to use this?
        //    int minComments = 10; // Replace with desired minimum likes
        //    int maxComments = Integer.MAX_VALUE; // Replace with desired maximum likes (Integer.MAX_VALUE for unlimited)
        //    Sort sort = Sort.by(Sort.Direction.ASC, "commentsCount");
        //    List<Post> posts = postRepository.findAll(hasCommentsInRange(minComments, maxComments), sort);
                return (root, query, criteriaBuilder) -> {
                    return criteriaBuilder.between(root.get("commentsCount"), minComments, maxComments);
                };
            }


}
