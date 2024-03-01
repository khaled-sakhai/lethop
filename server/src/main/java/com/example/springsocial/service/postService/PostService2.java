package com.example.springsocial.service.postService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.validator.permessions.PostOwner;
import com.example.springsocial.util.Constants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.repository.PostRepo;
import com.example.springsocial.specification.PostSpecification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
public class PostService2 {
    
    @Autowired
    private PostRepo postRepo;
    @PersistenceContext
    private EntityManager entityManager;

     public Page<Post> getFeedPosts(String category,String TagName,
                                       int pageNo,int pageSize,String sortBy,String sortDirection){

        Specification<Post> spec = userSpec(category, TagName);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        
        return postRepo.findAll(spec,paging);
    }

    public Post getPostById(Long postId){
        Optional<Post> post = postRepo.findById(postId);
        return post.orElseThrow();
    }

    public void addPost(Post post, User user, Set<Tag> tags, Category category){
        post.setUser(user);
        category.getPosts().add(post);
        post.setCategory(category);
        post.setListTags(tags);

        if(user.addPost(post)){
            postRepo.save(post);
        }
    }


    @PostOwner
    public void deletePost(Post post){
        postRepo.delete(post);
    }

    @PostOwner
    public void updatePost(Post post){


        postRepo.save(post);
    }

    public Optional<Post> findPostById(Long postId){
         return postRepo.findById(postId);
    }

    @PostOwner
    public Page<Post> findPostsByUserId(Long userId,int pageNo,int pageSize,String sortBy,String sortDirection){

        Specification<Post> spec = Specification.where(PostSpecification.postByUserId(userId));
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);

         return postRepo.findAll(spec,paging);
    }

    @SuppressWarnings({"unchecked", "DuplicatedCode"})

    public List<Post> searchPosts(String searchText) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Post.class)
                .get();

        org.apache.lucene.search.Query luceneQuery = queryBuilder
                .keyword()
                .onField("title").boostedTo(2)  // Boost title matches for better relevance
                .andField("content").boostedTo(1.5F)  // Boost content matches slightly
                .andField("listTags.tagName")
                .andField("category.category")  // Assuming searchable fields in Category
                .matching(searchText)
                .createQuery();

        Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Post.class);

        return jpaQuery.getResultList();
    }
    private Specification<Post> userSpec(String category,String TagName){
        return Specification
        .where(PostSpecification.postWithCategory(category, Constants.AllowedCategory[0]))
              .and(PostSpecification.postWithTag(TagName, Constants.AllowedTags[0]));
    }

    private Specification<Post> adminSpec(boolean isDelete,boolean isAnonymous,Long userId){
        return Specification.where(
            PostSpecification.isDeleted(isDelete)
        ).and(PostSpecification.isAnonymous(isDelete)).and(PostSpecification.postByUserId(userId))
        ;
    }


}
