package com.example.springsocial.controller.admin;


import com.example.springsocial.dto.ImageDto;
import com.example.springsocial.dto.user.UserDto;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.service.ImageService;
import com.example.springsocial.service.admin.AdminImage;
import com.example.springsocial.validator.validators.ValidPostSortBy;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class ImageAdminController {
    private final AdminImage adminImage;
    private final ImageService imageService;


    @GetMapping(value = "api/admin/image")
    public ResponseEntity<Page<ImageDto>> getAllImages(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                                      @RequestParam(defaultValue = "20") int size,
                                                      @RequestParam(defaultValue = "desc") String sortDirection){
        Page<Image> imagePage = adminImage.findAll(page, size, sortBy, sortDirection);
        if (!imagePage.isEmpty()) {
            Page<ImageDto> imageDtos= imagePage.map(ImageDto::new);
            return new ResponseEntity<>(imageDtos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "api/admin/image/deleted")
    public ResponseEntity<Page<ImageDto>> getAllDeletedImages(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                                      @RequestParam(defaultValue = "20") int size,
                                                      @RequestParam(defaultValue = "desc") String sortDirection){
        Page<Image> imagePage = adminImage.findDeletedImages(page, size, sortBy, sortDirection);
        if (!imagePage.isEmpty()) {
            Page<ImageDto> imageDtos= imagePage.map(ImageDto::new);
            return new ResponseEntity<>(imageDtos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "api/admin/image/{imageId}")
    public ResponseEntity<Image> getAnyImageById(@PathVariable Long imageId){
        Optional<Image> imageOptional = adminImage.findAnyImageById(imageId);
        if (imageOptional.isPresent()) {
            return new ResponseEntity<>(imageOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping(path = "api/admin/image/{imageId}")
    @Transactional
    public ResponseEntity<String> removeImage(@PathVariable long imageId,@RequestParam(required = false) boolean finalDelete) throws IOException {
        Optional<Image> imageOptional = adminImage.findAnyImageById(imageId);
        if (imageOptional.isPresent()) {
            if (finalDelete) {
                imageService.finalDeleteImages(imageOptional.get());
                adminImage.adminDeleteImage(imageOptional.get());
            } else {
                imageService.deletImage(imageOptional.get());
            }
            return ResponseEntity.ok().body("Image removed!");
        }
          return  ResponseEntity.badRequest().body("Image was not removed!");
        }





}
