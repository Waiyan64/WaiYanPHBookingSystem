package com.example.RestService.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.RestService.common.exception.ResourceNotFoundException;
import com.example.RestService.entity.ClassSchedule;
import com.example.RestService.entity.Country;
import com.example.RestService.repository.ClassScheduleRepository;
import com.example.RestService.repository.CountryRepository;
import com.example.RestService.web.request.ClassCreateRequest;
import com.example.RestService.web.response.ClassScheduleResponse;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/classes")
@Tag(name = "Class", description = "Class management")
public class ClassController {
    private final ClassScheduleRepository classScheduleRepository;
    private final CountryRepository countryRepository;

    public ClassController(ClassScheduleRepository classScheduleRepository, CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
        this.classScheduleRepository = classScheduleRepository;
    }

    @GetMapping
    public ResponseEntity<List<ClassSchedule>> getClasses() {
        return ResponseEntity.ok(classScheduleRepository.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<ClassScheduleResponse> postMethodName(@RequestBody ClassCreateRequest classCreateRequest) {

        Country country = countryRepository.findByCode(classCreateRequest.getCountryCode())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
        ClassSchedule classSchedule = new ClassSchedule();
        classSchedule.setTitle(classCreateRequest.getTitle());
        classSchedule.setDescription(classCreateRequest.getDescription());
        classSchedule.setStartTime(classCreateRequest.getStartTime());
        classSchedule.setEndTime(classCreateRequest.getEndTime());
        classSchedule.setRequiredCredits(classCreateRequest.getRequiredCredits());
        classSchedule.setTotalSlots(classCreateRequest.getTotalSlots());
        classSchedule.setBookedSlots(classCreateRequest.getBookedSlots());
        classSchedule.setCreatedDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        classSchedule.setUpdatedDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        classSchedule.setCountry(country);

        ClassSchedule savedClass = classScheduleRepository.save(classSchedule);
        ClassScheduleResponse response = new ClassScheduleResponse(savedClass);   

        return ResponseEntity.ok(response);
    }
    
    
    
}
