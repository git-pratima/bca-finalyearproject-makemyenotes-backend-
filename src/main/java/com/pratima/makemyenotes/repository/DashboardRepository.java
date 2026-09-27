package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {

    Optional<Dashboard> findByUserId(Long userId);

}