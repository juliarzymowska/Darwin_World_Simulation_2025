package agh.ics.oop.model.observators;

import agh.ics.oop.model.stats.StatsRecord;

/**
 * Interface for classes that listen to changes in statistics.
 */
public interface StatsChangeListener {
    /**
     * Called when the statistics have changed.
     */
    void statsChanged(StatsRecord stats);
}
