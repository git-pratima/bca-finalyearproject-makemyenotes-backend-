package com.pratima.makemyenotes.controller;

import com.pratima.makemyenotes.dto.EventDTO;
import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.service.EventService;
import com.pratima.makemyenotes.utility.UserUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    UserUtility userUtility;

    Response response = null;


    @PostMapping("/save-event")
    public ResponseEntity<Response> saveEvent(@RequestBody EventDTO eventDTO){
        try {
            response = eventService.saveEvent(eventDTO);
        }catch (Exception e){
            return ResponseEntity.ok(Response.builder()
                    .status(500)
                    .message("Event creation Failed.")
                    .build());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-user-event")
    public ResponseEntity<Response> getUserEvents(){
        try {
            response = eventService.getUserEvents(userUtility.getLoggedInUser().getId());
        }catch (Exception e){
            return ResponseEntity.ok(Response.builder()
                    .status(500)
                    .message("Event retrieval Failed.")
                    .build());
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<Response> deleteEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.deleteEvent(id));
    }

}
