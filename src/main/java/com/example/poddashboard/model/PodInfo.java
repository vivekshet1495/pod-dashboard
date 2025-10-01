package com.example.poddashboard.model;

// Java 17 record
public record PodInfo(
        String name,
        String namespace,
        String status,
        int containerCount
) {}
