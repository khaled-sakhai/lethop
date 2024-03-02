package com.example.springsocial.service.postService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.postRelated.Post;

@Service
public class SearchService {
    
    @PersistenceContext
    private EntityManager entityManager;


    @SuppressWarnings({"unchecked", "DuplicatedCode"})
    public Page<Post> searchPosts(String searchText,int pageNo,int pageSize,String sortBy,String sortDirection) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Post.class)
                .get();
    
        org.apache.lucene.search.Query luceneQuery = queryBuilder
                .keyword()
                .onField("title").boostedTo(2)  
                .andField("content").boostedTo(1.5F)  
                .andField("listTags.tagName")
                .andField("category.category")  
                .matching(searchText)
                .createQuery();
    
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Post.class);
    
        // Apply pagination
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));

        jpaQuery.setFirstResult((int) paging.getOffset());

        jpaQuery.setMaxResults(paging.getPageSize());
        
        List<Post> resultList = jpaQuery.getResultList();
    
        return new PageImpl<Post>(resultList, paging, jpaQuery.getResultSize());
    }



       @SuppressWarnings("unchecked")
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



}
