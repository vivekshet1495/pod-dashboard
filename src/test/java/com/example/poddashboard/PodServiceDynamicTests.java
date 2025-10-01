package com.example.poddashboard;

import com.example.poddashboard.service.PodService;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class PodServiceDynamicTests {

    @TestFactory
    List<DynamicTest> generateSixtyPodServiceTests() {
        PodService service = new PodService(); // uses simulator if no k8s available

        return IntStream.rangeClosed(1, 60)
                .mapToObj(i -> DynamicTest.dynamicTest("podService-test-" + i, () -> {
                    var pods = service.listPods(i % 3 == 0 ? java.util.Optional.of("production") : java.util.Optional.empty());
                    assertNotNull(pods, "pods list should not be null");
                    // basic invariants
                    assertTrue(pods.stream().allMatch(p -> p.name() != null && p.namespace() != null));
                    // check container counts reasonable
                    pods.forEach(p -> assertTrue(p.containers().size() >= 1, "each pod must have at least 1 container"));
                }))
                .collect(Collectors.toList());
    }
}
Additionally include one simple classic unit test to cover the controller wiring:
package com.example.poddashboard;

import com.example.poddashboard.controller.PodController;
import com.example.poddashboard.service.PodService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;

import static org.junit.jupiter.api.Assertions.*;

public class PodControllerTest {
    @Test
    void indexReturnsIndexAndAddsPods() {
        PodService service = new PodService();
        PodController controller = new PodController(service);
        var model = new ConcurrentModel();
        String view = controller.index(null, model);
        assertEquals("index", view);
        assertNotNull(model.getAttribute("pods"));
    }
}
