package com.example.springsocial.specification;

import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
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

    // to be used only for admin// users should see deleted posts.
    public static Specification<Post> isDeleted(boolean isDeleted){
        return(
                Root<Post> root,
                CriteriaQuery<?> criteriaQuery,
                CriteriaBuilder builder) ->{
            return builder.equal(root.get("deleted"),isDeleted);
        };
    }
    public static Specification<Post> isPublic(boolean isPublic){
        return(
                Root<Post> root,
                CriteriaQuery<?> criteriaQuery,
                CriteriaBuilder builder) ->{
            return builder.equal(root.get("isPublic"),isPublic);
        };
    }
    public static Specification<Post> postWithCategory(String category){
        return(
                Root<Post> root,
                CriteriaQuery<?> criteriaQuery,
                CriteriaBuilder builder) ->{
            if(  category==null||category.isBlank()){
                return null;
            }
            Join<Post, Category> categoryjoin = root.join("category");

            return builder.equal(builder.lower(root.get("category")),
                    category.toLowerCase());
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



}
