package com.pratima.makemyenotes.controller;

import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard-data")
    public ResponseEntity<Response> getDashboardData(){
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("success")
                .dashboard(dashboardService.getDashboardData())
                .build());
    }

}
