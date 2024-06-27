package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.service.EmailService;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam("subject") String subject, 
                            @RequestParam("message") String message, 
                            Model model) {
        emailService.sendEmailToAll(subject, message);
        model.addAttribute("success", "Emails sent successfully!");
        return "dashboard";
    }
}
