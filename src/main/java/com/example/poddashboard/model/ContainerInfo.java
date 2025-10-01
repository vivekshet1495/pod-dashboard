package com.example.poddashboard.model;

// Java 17 record
public record ContainerInfo(
        String name,
        String image,
        String state,
        String ready
) {}
