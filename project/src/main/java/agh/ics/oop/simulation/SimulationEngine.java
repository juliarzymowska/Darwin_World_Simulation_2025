package agh.ics.oop.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final ForkJoinPool executorService = ForkJoinPool.commonPool();
    private final List<Simulation> simulationList = new ArrayList<>();

    public void addSimulation(Simulation simulation) {
        simulationList.add(simulation);
        executorService.submit(simulation);
    }

    public void shutdownService() {
        for (Simulation simulation : simulationList) {
            simulation.stop();
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
