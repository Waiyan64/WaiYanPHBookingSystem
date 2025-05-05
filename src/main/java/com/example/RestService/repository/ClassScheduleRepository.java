package com.example.RestService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.RestService.entity.ClassSchedule;
import com.example.RestService.entity.Country;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
    
    List<ClassSchedule> findByCountryAndStartTimeAfterAndActiveTrue(Country country, LocalDateTime now);

    @Query("SELECT c FROM ClassSchedule c WHERE c.active = true AND c.endTime > :now ORDER BY c.startTime ASC")
    List<ClassSchedule> findUpComingClasses(LocalDateTime now);

    @Query("SELECT c FROM ClassSchedule c WHERE c.active = true AND c.endTime > :now " +
           "AND c.country = :country ORDER BY c.startTime ASC")
    List<ClassSchedule> findUpcomingClassesByCountry(Country country, LocalDateTime now);

    @Query("SELECT c FROM ClassSchedule c WHERE c.endTime < :now AND c.active = true")
    List<ClassSchedule>findCompletedClasses(LocalDateTime now);
}
