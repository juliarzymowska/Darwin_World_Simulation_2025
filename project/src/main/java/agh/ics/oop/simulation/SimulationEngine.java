package agh.ics.oop.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> sims;
    private final List<Thread> threads = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public SimulationEngine(List<Simulation> sims) {
        this.sims = sims;
    }

    public void runSync() {
        for (Simulation sim : sims) {
            sim.run();
        }
    }

    public void runAsync() {
        for (Simulation sim : sims) {
            Thread thread = new Thread(sim);
            threads.add(thread);
            thread.start();
        }
    }

    public void awaitSimulationsEnd() {
        try {
            for (Thread thread : threads) {
                thread.join();
            }

            executorService.shutdown();
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runAsyncInThreadPool() {
        for (Simulation sim : sims) {
            executorService.submit(sim);
        }
    }
}
