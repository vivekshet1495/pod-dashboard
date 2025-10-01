package com.example.poddashboard.controller;

import com.example.poddashboard.model.PodInfo;
import com.example.poddashboard.service.PodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PodController {

    private final PodService podService;

    public PodController(PodService podService) {
        this.podService = podService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<PodInfo> pods = podService.getAllPods();
        model.addAttribute("pods", pods);
        return "index"; // points to src/main/resources/templates/index.html
    }

}
