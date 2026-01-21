package agh.ics.oop.model.elements;

import agh.ics.oop.configuration.ConfigAnimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;


public class Genotype {
    protected List<Integer> genotype = new ArrayList<>();
    protected int activeGeneIndex;
    private final static Random random = new Random();

    /*
     * Constructor of Genotype class.
     * For animals to be placed on map at the beginning of simulation.
     * @param genotypeLength - length of genotype
     * @return new random genotype
     * */
    public Genotype(int genotypeLength) {
        for (int i = 0; i < genotypeLength; i++) {
            this.genotype.add(random.nextInt(8));
        }
        activeGeneIndex = random.nextInt(genotypeLength);
    }

    /*
     * (for tests)
     * Constructor of Genotype class.
     * @param genotype - predefined genotype
     * @return new genotype with given genes
     * */
    public Genotype(List<Integer> genotype) {
        this.genotype = genotype;
        this.activeGeneIndex = random.nextInt(genotype.size());
    }

    /*
     * Genotype constructor for child animal created from two parents.
     * @param father - father animal
     * @param mother - mother animal
     * @return new genotype created from parents' genotypes
     * */
    public Genotype(Animal father, Animal mother, int minMutations, int maxMutations) {
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

        // TODO: change from loops to addAll with subList
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
        int mutationsNumber = random.nextInt(minMutations, maxMutations + 1);
        // Apply mutations
        mutateGenotype(this.genotype, mutationsNumber);
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

    private void mutateGenotype(List<Integer> genotype, int mutationsNumber) {
        // Create a list of indices from 0 to genotype length - 1
        List<Integer> indexes = IntStream.range(0, genotype.size())
                .boxed()
                .toList();

        for (int i = 0; i < mutationsNumber; i++) {
            int indexToMutate = indexes.get(i);
            int currentGene = genotype.get(indexToMutate);
            int mutatedGene = (currentGene + random.nextInt(1, 8)) % 8;
            genotype.set(indexToMutate, mutatedGene);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genotype genotype1 = (Genotype) o;
        return Objects.equals(this.genotype, genotype1.genotype);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genotype);
    }

    public String toString() {
        return genotype.toString();
    }
}
