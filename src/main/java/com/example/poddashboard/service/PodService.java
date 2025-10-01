package com.example.poddashboard.service;

import com.example.poddashboard.model.PodInfo;
import com.example.poddashboard.model.ContainerInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PodService {

    public List<PodInfo> getAllPods() {
        return List.of(
                new PodInfo("pod-1", "default", "Running", 2),
                new PodInfo("pod-2", "default", "Pending", 1)
        );
    }

    public List<ContainerInfo> getContainers(String podName) {
        return List.of(
                new ContainerInfo("container-1", "nginx:latest", "Running", "True"),
                new ContainerInfo("container-2", "redis:latest", "Running", "True")
        );
    }
}
