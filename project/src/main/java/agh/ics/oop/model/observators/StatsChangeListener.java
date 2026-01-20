package agh.ics.oop.model.observators;

import agh.ics.oop.model.stats.StatsRecord;

/**
 * Interfejs dla obserwatorów statystyk,
 * realizacja wzorca obserwator.
 */
public interface StatsChangeListener {
    /**
    * Metoda, która jest reakcją na powiadomienie o zmianie statystyk
     * @param stats obserwowane statystyki
     */
    void statsChanged(StatsRecord stats);
}
