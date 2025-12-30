package agh.ics.oop.model.observators;

import agh.ics.oop.model.map.WorldMap;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, String message);
}
