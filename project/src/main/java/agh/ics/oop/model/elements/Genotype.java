package agh.ics.oop.model.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Genotype {
    protected List<Integer> genotype = new ArrayList<>();
    protected int activeGeneIndex;
    private final static Random random = new Random();

    /*
     * Constructor of Genotype class.
     * For animals to be placed on map at the beginning of simulation.
     * @param genotypeLength - length of genotype
     * @return new random genotype
     * TODO: move to RandomGenotypeGenerator
     * */
    public Genotype(int genotypeLength) {
        for (int i = 0; i < genotypeLength; i++) {
            this.genotype.add(random.nextInt(8));
        }
        activeGeneIndex = random.nextInt(genotypeLength);
    }

    /*
     * Genotype constructor for child animal created from two parents.
     * @param father - father animal
     * @param mother - mother animal
     * @return new genotype created from parents' genotypes
     * TODO: move to RandomGenotypeGenerator
     * */
    public Genotype(Animal father, Animal mother) {
        int genotypeLength = father.getGenotype().getGenotypeList().size();

        // Calculate energy proportions
        int fatherEnergy = father.getEnergy();
        int motherEnergy = mother.getEnergy();
        int totalEnergy = fatherEnergy + motherEnergy;

        // Determine which parent is stronger
        Animal stronger = fatherEnergy >= motherEnergy ? father : mother;
        Animal weaker = fatherEnergy >= motherEnergy ? mother : father;

        double strongerRatio = (double) Math.max(fatherEnergy, motherEnergy) / totalEnergy;
        int strongerGeneCount = (int) Math.round(genotypeLength * strongerRatio);

        // Randomly choose side: true = right, false = left
        boolean rightSide = random.nextBoolean();

        if (rightSide) {
            // Stronger parent contributes from right side
            for (int i = 0; i < genotypeLength - strongerGeneCount; i++) {
                this.genotype.add(weaker.getGenotype().getGenotypeList().get(i));
            }
            for (int i = genotypeLength - strongerGeneCount; i < genotypeLength; i++) {
                this.genotype.add(stronger.getGenotype().getGenotypeList().get(i));
            }
        } else {
            // Stronger parent contributes from left side
            for (int i = 0; i < strongerGeneCount; i++) {
                this.genotype.add(stronger.getGenotype().getGenotypeList().get(i));
            }
            for (int i = strongerGeneCount; i < genotypeLength; i++) {
                this.genotype.add(weaker.getGenotype().getGenotypeList().get(i));
            }
        }
        // Apply mutations
        genotype = mutateGenotype(this.genotype, random.nextInt(8));
        // Set random active gene index
        activeGeneIndex = random.nextInt(genotypeLength);
    }

    // Getters
    public List<Integer> getGenotypeList() {
        return genotype;
    }

    public int getActiveGene() {
        return genotype.get(activeGeneIndex);
    }

    public int getActiveGeneIndex() {
        return activeGeneIndex;
    }

    // Methods
    public void moveToNextGene() {
        activeGeneIndex = (activeGeneIndex + 1) % genotype.size();
    }

    // Methods
    private List<Integer> mutateGenotype(List<Integer> genotype, int mutationsNumber) {
        // Create a list of indices from 0 to genotype length - 1
        List<Integer> indexes = java.util.stream.IntStream.range(0, genotype.size())
                .boxed()
                .toList();

        for (int i = 0; i < mutationsNumber; i++) {
            int indexToMutate = indexes.get(i);
            int currentGene = genotype.get(indexToMutate);
            int mutatedGene = (currentGene + random.nextInt(1, 8)) % 8;
            genotype.set(indexToMutate, mutatedGene);
        }

        return genotype;
    }
}
