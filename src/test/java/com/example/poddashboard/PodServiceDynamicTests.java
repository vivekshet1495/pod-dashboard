package com.example.poddashboard;

import com.example.poddashboard.model.PodInfo;
import com.example.poddashboard.service.PodService;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PodServiceDynamicTests {

    private final PodService podService = new PodService();

    @TestFactory
    Stream<DynamicTest> dynamicPodTests() {
        List<PodInfo> pods = podService.getAllPods();

        return pods.stream()
                .map(pod -> DynamicTest.dynamicTest(
                        "Test pod: " + pod.name(),
                        () -> assertEquals("default", pod.namespace())
                ));
    }
}
