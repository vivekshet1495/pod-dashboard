package com.example.poddashboard.controller;

import com.example.poddashboard.model.PodInfo;
import com.example.poddashboard.service.PodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class PodController {
    private final PodService podService;

    public PodController(PodService podService) {
        this.podService = podService;
    }

    @GetMapping("/")
    public String index(@RequestParam(name = "ns", required = false) String ns, Model model) {
        List<PodInfo> pods = podService.listPods(Optional.ofNullable(ns));
        model.addAttribute("pods", pods);
        return "index";
    }
}
________________________________________
Thymeleaf template index.html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Pod Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" th:href="@{/css/styles.css}">
</head>
<body class="bg-gradient">
<div class="container py-4">
    <h1 class="mb-4">Pod Dashboard</h1>

    <div class="row">
        <div th:each="pod : ${pods}" class="col-md-6 col-lg-4 mb-4">
            <div class="card pod-card h-100">
                <div class="card-body">
                    <h5 class="card-title">[[${pod.namespace}]] / [[${pod.name}]]</h5>
                    <p class="card-text">Phase: <span class="badge bg-info">[[${pod.phase}]]</span></p>
                    <p>Started: <small>[[${pod.startTime}]]</small></p>

                    <div th:each="c,iterStat : ${pod.containers}">
                        <div class="container-widget mb-2 p-2 rounded">
                            <div class="d-flex justify-content-between">
                                <strong>[[${c.name}]]</strong>
                                <span class="small">[[${c.state}]]</span>
                            </div>
                            <div class="small">Image: [[${c.image}]]</div>
                            <div class="mt-2">
                                <canvas th:attr="id=${pod.namespace + '_' + pod.name + '_' + iterStat.index + '_chart'}" width="200" height="80"></canvas>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
<script th:src="@{/js/chart-init.js}"></script>
</body>
</html>
