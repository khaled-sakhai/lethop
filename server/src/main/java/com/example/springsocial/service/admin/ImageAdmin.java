package com.example.springsocial.service.admin;

import com.example.springsocial.entity.Image;
import com.example.springsocial.repository.ImageRepo;
import com.example.springsocial.service.FireBaseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageAdmin {

    private final ImageRepo imageRepo;
    private final FireBaseService fireBaseService;

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

    @Transactional
    public void adminDeleteImage(Image image) throws IOException {
        if (image.getFileName()!="oauth2-image"){
            fireBaseService.deleteFile(image.getFileName());
        }
        imageRepo.adminDeleteById(image.getId());
    }

}
