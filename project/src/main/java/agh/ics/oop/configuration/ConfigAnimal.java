package agh.ics.oop.configuration;

/*
 * Class representing configuration parameters for animals in the simulation.
 * It includes settings for initial counts, energy levels, reproduction, and genetics.
 * TODO: add exceptions so maxEnergy > initialEnergy, minMutations <= maxMutations, initialAnimalCount > 0,
 *  genotypeLength > 0, energyToReproduce > 0 and energyToReproduce < maxEnergy, energyConsumedByMove > 0,
 *  energyGainedByEating > 0, etc!!
 * */
// TODO: make exceptions for invalid config values (like negative energy, zero genotype length, etc.)
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
    //  (for testing) Default constructor with preset values
    public ConfigAnimal() {
        this(1, 100, 100, 50, 50, 20, 1, 3, 8);
    }

    public ConfigAnimal(
            int initialAnimalCount,
            int initialEnergy,
            int maxEnergy,
            int energyToReproduce,
            int energyConsumedByMove,
            int energyGainedByEating,
            int minMutations,
            int maxMutations,
            int genotypeLength
    ) {
        this.initialAnimalCount = initialAnimalCount;
        this.initialEnergy = initialEnergy;
        this.maxEnergy = maxEnergy;
        this.energyToReproduce = energyToReproduce;
        this.energyConsumedByMove = energyConsumedByMove;
        this.energyGainedByEating = energyGainedByEating;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genotypeLength = genotypeLength;
    }
}
