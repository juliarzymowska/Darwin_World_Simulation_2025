package agh.ics.oop.simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public SimulationEngine() {
    }

    public void runAsyncInThreadPool(Simulation sim) {
        executorService.submit(sim);
    }

    public void shutdownService() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
