package agh.ics.oop.configuration;

/*
 * Class representing configuration parameters for animals in the simulation.
 * It includes settings for initial counts, energy levels, reproduction, and genetics.
 */
public record ConfigAnimal(
        int initialAnimalCount,
        int initialEnergy, // doesn't have to be equal to maxEnergy! (i think)
        int maxEnergy, // will be useful to make a progress bar later in gui
        int energyToReproduce,
        int energyConsumedByMove,
        int energyGainedByEating,
        int minMutations,
        int maxMutations,
        int genotypeLength
) {
    //  Default constructor with preset values
    public ConfigAnimal() {
        this(1, 100, 200, 50, 50, 20, 1, 3, 8);
    }
}
