package com.example.springsocial.service.admin;

import com.example.springsocial.entity.Image;
import com.example.springsocial.repository.ImageRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminImage {

    private final ImageRepo imageRepo;

    public Page<Image> findDeletedImages(int pageNo, int pageSize, String sortBy, String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
         return  imageRepo.adminFindAllDeleted(paging);
    }

    public  Page<Image>  findAll(int pageNo, int pageSize, String sortBy, String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
       return imageRepo.findAll(paging);
    }

    public Optional<Image> findAnyImageById(Long id){
        return imageRepo.adminFindById(id);
    }

    public void adminDeleteImage(Image image){
        imageRepo.adminDeleteById(image.getId());
    }

}
