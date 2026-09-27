package com.pratima.makemyenotes.controller;

import com.pratima.makemyenotes.entity.TempEntity;
import com.pratima.makemyenotes.repository.TempRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/temp")
public class TempController {

    @Autowired
    TempRepository tempRepository;

    @GetMapping("/message")
    public String saveMessage(){
        tempRepository.deleteAll();
        TempEntity temp = new TempEntity();
        temp.setMessage("New Trigger");
        temp.setLastrun(new Date());
        tempRepository.save(temp);
        System.out.println("Triggered Temp message.");
        return "Triggered Temp message.";
    }

}
