package com.example.springsocial.service.postService;

import java.util.List;
import java.util.Optional;

import com.example.springsocial.service.permessions.PostOwner;
import com.example.springsocial.specification.PostSpecification;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.springsocial.entity.postRelated.Post;

import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.ImageRepo;
import com.example.springsocial.repository.PostRepo;
import com.example.springsocial.service.GoogleCloudService;
import com.example.springsocial.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private GoogleCloudService googleCloudService;

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private UserService userService;
    @PersistenceContext
    private EntityManager entityManager;
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

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Post.class);

        return jpaQuery.getResultList();
    }
    public List<Post> getbyLikes(){
        Specification<Post> spec = Specification.where(PostSpecification.hasLikesInRange(0,100));
        return postRepo.findAll(spec);
    }


    public Post createNewPost(Post post){  
        return postRepo.save(post);
    }

    public Post updatePost(Post post){
        return postRepo.save(post);
    }

    public Optional<Post> findAnyPostById(Long id){
        return postRepo.findById(id);
    }

    
    public Optional<Post> findById(Long id){
        return postRepo.findPostById(id);
    }

    public Page<Post> findByUserId(Long userId,Pageable pageable){
        return postRepo.findPostsByUserId(userId,pageable);
    }

    public Page<Post> findByTag(String tag,Pageable pageable){
        return postRepo.findByListTagsTagName(tag,pageable);
    }

    public Page<Post> findByCategory(String category,Pageable pageable){
        return postRepo.findByCategoryCategory(category,pageable);
    }

    //feed page
    public Page<Post> findPostsByTagOrCategory(String tag,String category,Pageable pageable){
        return postRepo.findByListTagsTagNameOrCategoryCategory(tag,category,pageable);
    }

    public boolean isPostedByUser(User user,Post post){
        if(post.getUser()==user && user.getPosts().contains(post)){
            return true;
        }
        return false;
    }


    public void removePostById(Long id){
         postRepo.deleteById(id);
    }

    @PostOwner
    public void deletePost(Post post){
        postRepo.delete(post);
    }


    public void savePost(Post post,User user) throws Exception{
        if(user.getSavedPostsCount()>=10){
          throw new Exception("You can't save more than 10 posts, remove posts from your list to be able to save this post");
        }
        if(user.getSavedPosts().contains(post)){
              throw new Exception("This post is already in your saved list");
        }

        user.getSavedPosts().add(post);
        user.updateSavedCounter();
        post.getSavedByUsers().add(user);
        post.updateSavedCount();
        userService.updateUser(user);
        postRepo.save(post);
    }

    public void unsavePost(User user,Post post) throws Exception{
        if(!user.getSavedPosts().contains(post)){     
            throw new Exception("This post is not on your saved posts list, you can't unsave what's not saved");
        }
        user.getSavedPosts().removeIf(p->p.getId()==post.getId());        
        post.getSavedByUsers().removeIf(u->u.getId()==user.getId());

        user.updateSavedCounter();
        post.updateSavedCount();
        userService.updateUser(user);
        postRepo.save(post);
    }


    public void likePost(User user,Post post) throws Exception{
        if(user.getLikedPosts().contains(post)){
            throw new Exception("you've already liked this post");
        }
        user.getLikedPosts().add(post);
        post.getLikedByUsers().add(user);;

        user.updateLikedCounter();
        post.updateLikedCount();
        userService.updateUser(user);
        postRepo.save(post);
    }

    public void unlikePost(User user,Post post) throws Exception{
        if(!user.getLikedPosts().contains(post)){
            throw new Exception("you're already unliked this post, no need to unlinke it again");
        }
        user.getLikedPosts().removeIf(p-> p.getId()==post.getId());
        post.getLikedByUsers().removeIf(u-> u.getId()==user.getId());
        user.updateLikedCounter();
        post.updateLikedCount();
        userService.updateUser(user);
        postRepo.save(post);
    }




}
