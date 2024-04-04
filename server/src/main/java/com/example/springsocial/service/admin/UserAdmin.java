package com.example.springsocial.service.admin;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.specification.UserSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserAdmin {
    private final UserRepo userRepo;

    public Page<User> findAll(Boolean isActive,String provider,Boolean needProfileUpdate,Long userId,int pageNo, int pageSize, String sortBy, String sortDirection){
        Specification<User> spec=this.userSpec(isActive,provider,needProfileUpdate,userId);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);

        return userRepo.findAll(spec,paging);
    }
    public Optional<User> findAnyUserById(long userId){
        return userRepo.findAnyUserById(userId);
    }


    public Page<User> findDeleted(int pageNo,int pageSize,String sortBy,String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        return userRepo.findAllDeleted(paging);
    }

    public void finalDeleteById(User user){
        user.getUserInterests().clear();
        user.getUserReplies().clear();
        user.getPosts().clear();
        user.getLikedPosts().clear();
        user.getSavedPosts().clear();
        user.getRoles().clear();
        user.getUserComments().clear();
        user.getTokens().forEach(t->t.setUser(null));
        user.getTokens().clear();
       userRepo.deleteUserById(user.getId());
    }

    private Specification<User> userSpec(Boolean isActive,String provider,Boolean needProfileUpdate,Long userId){
        return Specification.where(UserSpecification.byActive(isActive)
                        .and(UserSpecification.byProvider(provider))
                        .and(UserSpecification.byProfileUpdated(needProfileUpdate))
                        .and(UserSpecification.byUserId(userId))
                );
    }
}
