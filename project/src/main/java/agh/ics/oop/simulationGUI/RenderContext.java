package agh.ics.oop.simulationGUI;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;

import java.util.Set;

/*
 * Class that holds the context for rendering the simulation GUI.
 * It includes information about the currently tracked animal,
 * top genotypes, and display preferences.
 * */
public record RenderContext(
        Animal trackedAnimal,
        Set<Genotype> topGenotypes,
        boolean showPreferredFields,
        boolean showDominantGenotype
) {
}