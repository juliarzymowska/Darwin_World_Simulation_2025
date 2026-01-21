package agh.ics.oop.simulation;

import agh.ics.oop.configuration.ConfigAnimal;
import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.map.EarthMap;
import agh.ics.oop.model.map.FeromonMap;
import agh.ics.oop.model.map.MapType;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.stats.CSVSaver;
import agh.ics.oop.model.stats.SimulationStatsTracker;
import agh.ics.oop.model.util.Vector2d;

import java.util.List;

public class Simulation implements Runnable {
    private final WorldMap map;
    private final SimulationStatsTracker statsTracker;
    private final int moveDelay;

    // Thread control
    private volatile boolean running = false;
    private volatile boolean paused = true;
    private final Object pauseLock = new Object();

    public Simulation(ConfigAnimal configAnimal, ConfigMap configMap, int moveDelay) {
        this.moveDelay = moveDelay;
        this.map = (configMap.mapType() == MapType.EARTH_MAP) ? new EarthMap(configMap) : new FeromonMap(configMap);
        this.statsTracker = new SimulationStatsTracker(map);
        map.addObserver(statsTracker);
        statsTracker.addObserver(new CSVSaver(map));

        generateAnimalsOnMap(configAnimal, configMap, map);
    }

    @Override
    public void run() {
        running = true;
        int currentDay = 0;

        while (running) {
            // Handle Pause
            synchronized (pauseLock) {
                while (paused && running) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            // Logic
            currentDay++;
            performDayCycle(currentDay);

            try {
                Thread.sleep(moveDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void performDayCycle(int currentDay) {
        map.removeDeadAnimals(currentDay);
        List<Animal> animals = map.getElementsManager().getAnimals();
        for (Animal animal : animals) {
            map.moveTo(animal);
        }
        map.consumePlants();
        map.reproduceAnimals(currentDay);
        map.growDailyPlants();

        if (map instanceof FeromonMap feromonMap) {
            feromonMap.decreaseFeromons();
        }

        // Stats are updated via observers automatically,
        // but we trigger mapChanged to notify UI
        map.mapChanged(map, "Day " + currentDay);
        statsTracker.updateStats(currentDay);

        if (animals.isEmpty()) {
            running = false;
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public void shutDown() {
        running = false;
        resume(); // break wait if paused
    }

    public WorldMap getMap() {
        return map;
    }

    public SimulationStatsTracker getStats() {
        return statsTracker;
    }

    private void generateAnimalsOnMap(ConfigAnimal configAnimal, ConfigMap configMap, WorldMap map) {
        for (int i = 0; i < configAnimal.initialAnimalCount(); i++) {
            Vector2d position = new Vector2d(
                    (int) (Math.random() * configMap.width()),
                    (int) (Math.random() * configMap.height())
            );
            Animal newAnimal = new Animal(position, configAnimal);
            // safe check if map allows placement (though here we just force it usually)
            if (!map.isOccupied(position) || map.canMoveTo(position)) {
                map.placeAnimal(newAnimal);
            }
        }
    }
}