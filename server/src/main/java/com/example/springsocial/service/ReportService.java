package com.example.springsocial.service;

import com.example.springsocial.entity.Features.Report;
import com.example.springsocial.repository.ReportRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.service.postService.PostService2;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportService {

    private final ReportRepo reportRepo;
    private final UserService userService;
    private final UserRepo userRepo;
    private final PostService2 postService2;


    public Report createReport(Report report){
        return reportRepo.save(report);
    }
    public List<Report> getAllReports(){
        return reportRepo.findAll();
    }
    public Optional<Report> findById(Long id){
        return reportRepo.findById(id);
    }
    public void deleteReport(Long id){
        reportRepo.deleteById(id);
    }


}

