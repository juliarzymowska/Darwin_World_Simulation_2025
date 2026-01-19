package agh.ics.oop.model.stats;

import agh.ics.oop.model.elements.Genotype;

import java.util.List;

public record StatsRecord(int day, int animalCount, int plantCount,int freeTilesCount, List<Genotype> mostPopularGenotypes, double averageEnergyLevel, double averageLifeTime, double averageKids) {
}
