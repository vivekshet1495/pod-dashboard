package com.example.poddashboard.service;

import com.example.poddashboard.model.ContainerInfo;
import com.example.poddashboard.model.PodInfo;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PodService {
    private static final Logger log = LoggerFactory.getLogger(PodService.class);
    private final KubernetesClient k8sClient;

    public PodService() {
        KubernetesClient client;
        try {
            client = new KubernetesClientBuilder().build();
            log.info("Kubernetes client initialized");
        } catch (Exception e) {
            log.warn("Kubernetes client not available, using simulator: {}", e.getMessage());
            client = null;
        }
        this.k8sClient = client;
    }

    public List<PodInfo> listPods(Optional<String> namespaceFilter) {
        if (k8sClient == null) {
            return simulatorData(namespaceFilter);
        }

        var pods = new ArrayList<PodInfo>();
        var namespaceList = namespaceFilter.map(List::of).orElseGet(() -> k8sClient.namespaces().list().getItems().stream().map(ns -> ns.getMetadata().getName()).toList());
        for (var ns : namespaceList) {
            var podList = k8sClient.pods().inNamespace(ns).list().getItems();
            for (Pod p : podList) {
                var meta = p.getMetadata();
                var status = p.getStatus();
                var containers = new ArrayList<ContainerInfo>();
                if (p.getSpec() != null && p.getSpec().getContainers() != null) {
                    p.getSpec().getContainers().forEach(c -> {
                        String name = c.getName();
                        String image = c.getImage();
                        String state = "unknown";
                        if (status != null && status.getContainerStatuses() != null) {
                            status.getContainerStatuses().stream().filter(cs -> cs.getName().equals(name)).findFirst().ifPresent(cs -> {
                                if (cs.getState() != null) {
                                    if (cs.getState().getRunning() != null) state = "running";
                                    else if (cs.getState().getWaiting() != null) state = "waiting";
                                    else if (cs.getState().getTerminated() != null) state = "terminated";
                                }
                            });
                        }
                        containers.add(new ContainerInfo(name, image, state, Math.random() * 500, 50 + Math.random() * 300));
                    });
                }
                OffsetDateTime started = status != null && status.getStartTime() != null ? OffsetDateTime.parse(status.getStartTime()) : OffsetDateTime.now();
                pods.add(new PodInfo(ns, meta.getName(), status != null ? status.getPhase() : "Unknown", started, containers));
            }
        }
        return pods;
    }

    private List<PodInfo> simulatorData(Optional<String> namespaceFilter) {
        var pods = new ArrayList<PodInfo>();
        String[] namespaces = {"default", "kube-system", "production"};
        for (String ns : namespaces) {
            if (namespaceFilter.isPresent() && !namespaceFilter.get().equals(ns)) continue;
            for (int i = 1; i <= 3; i++) {
                List<ContainerInfo> containers = new ArrayList<>();
                int contCount = 1 + (i % 3);
                for (int c = 1; c <= contCount; c++) {
                    containers.add(new ContainerInfo("app-" + c, "example/image:v" + i, c % 2 == 0 ? "running" : "waiting", Math.random() * 250, 32 + Math.random() * 250));
                }
                pods.add(new PodInfo(ns, ns + "-pod-" + i, i % 2 == 0 ? "Running" : "Pending", OffsetDateTime.now().minusHours(i), containers));
            }
        }
        return pods;
    }
}
