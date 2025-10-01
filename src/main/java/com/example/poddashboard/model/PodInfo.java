package com.example.poddashboard.model;

import java.util.List;

public record ContainerInfo(String name, String image, String state, double cpu, double memoryMb) {}
package com.example.poddashboard.model;

import java.time.OffsetDateTime;
import java.util.List;

public record PodInfo(String namespace, String name, String phase, OffsetDateTime startTime, List<ContainerInfo> containers) {}
