package agh.ics.oop.configuration;

/*
 * Class representing configuration parameters for animals in the simulation.
 * It includes settings for initial counts, energy levels, reproduction, and genetics.
 * */
public record ConfigAnimal(
        int initialAnimalCount,
        int initialEnergy,
        int energyToReproduce,
        int energyConsumedByMove,
        int energyGainedByEating,
        int minMutations,
        int maxMutations,
        int genotypeLength
) {
    //  (for testing) Default constructor with preset values
    public ConfigAnimal() {
        this(1, 100, 50, 50, 20, 1, 3, 8);
    }

    public ConfigAnimal(
            int initialAnimalCount,
            int initialEnergy,
            int energyToReproduce,
            int energyConsumedByMove,
            int energyGainedByEating,
            int minMutations,
            int maxMutations,
            int genotypeLength
    ) {
        this.initialAnimalCount = initialAnimalCount;
        this.initialEnergy = initialEnergy;
        this.energyToReproduce = energyToReproduce;
        this.energyConsumedByMove = energyConsumedByMove;
        this.energyGainedByEating = energyGainedByEating;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genotypeLength = genotypeLength;
    }
}
