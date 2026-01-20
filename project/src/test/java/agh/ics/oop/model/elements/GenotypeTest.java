package agh.ics.oop.model.elements;

import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenotypeTest {

    @Test
    void shouldCreateGenotypeWithValidLength() {
        Genotype genotype = new Genotype(8);
        assertNotNull(genotype);
        assertEquals(8, genotype.getGenotypeList().size());
    }

    @Test
    void shouldCreateGenotypeWithSingleGene() {
        Genotype genotype = new Genotype(1);
        assertEquals(1, genotype.getGenotypeList().size());
    }

    @Test
    void shouldGenerateGenesInValidRange() {
        Genotype genotype = new Genotype(100);
        for (int gene : genotype.getGenotypeList()) {
            assertTrue(gene >= 0 && gene < 8, "Gene value should be between 0 and 7");
        }
    }

    @Test
    void shouldSetRandomActiveGeneIndex() {
        Genotype genotype = new Genotype(10);
        int activeIndex = genotype.getActiveGeneIndex();
        assertTrue(activeIndex >= 0 && activeIndex < 10);
    }

    @Test
    void shouldGetActiveGene() {
        Genotype genotype = new Genotype(8);
        int activeGene = genotype.getActiveGene();
        assertTrue(activeGene >= 0 && activeGene < 8);
    }

    @Test
    void shouldMoveToNextGene() {
        Genotype genotype = new Genotype(5);
        int initialIndex = genotype.getActiveGeneIndex();
        genotype.moveToNextGene();
        assertEquals((initialIndex + 1) % 5, genotype.getActiveGeneIndex());
    }

    @Test
    void shouldWrapAroundWhenMovingToNextGene() {
        Genotype genotype = new Genotype(3);
        // Move to last position
        while (genotype.getActiveGeneIndex() != 2) {
            genotype.moveToNextGene();
        }
        genotype.moveToNextGene();
        assertEquals(0, genotype.getActiveGeneIndex());
    }

    @Test
    void shouldCreateChildGenotype() {
        Animal father = new Animal(new Vector2d(0, 0));
        Animal mother = new Animal(new Vector2d(0, 0));
        father.setCurrentEnergy(100);
        mother.setCurrentEnergy(100);

        Genotype childGenotype = new Genotype(father, mother, 0, 0);

        assertNotNull(childGenotype);
        assertEquals(father.getGenotype().getGenotypeList().size(),
                childGenotype.getGenotypeList().size());
    }

    @Test
    void shouldInheritGenesFromParents() {
        Animal father = new Animal(new Vector2d(0, 0));
        Animal mother = new Animal(new Vector2d(0, 0));
        father.setCurrentEnergy(80);
        mother.setCurrentEnergy(120);

        Genotype childGenotype = new Genotype(father, mother, 0, 0);

        for (int gene : childGenotype.getGenotypeList()) {
            assertTrue(gene >= 0 && gene < 8);
        }
    }

    @Test
    void shouldHandleEqualParentEnergies() {
        Animal father = new Animal(new Vector2d(0, 0));
        Animal mother = new Animal(new Vector2d(0, 0));
        father.setCurrentEnergy(100);
        mother.setCurrentEnergy(100);

        Genotype childGenotype = new Genotype(father, mother, 0, 0);

        assertNotNull(childGenotype);
        assertEquals(father.getGenotype().getGenotypeList().size(),
                childGenotype.getGenotypeList().size());
    }

//    @Test
//    TODO: make an error for genotype length less than 1
//    void shouldHandleZeroLengthGenotype() {
//        Genotype genotype = new Genotype(0);
//        assertEquals(0, genotype.getGenotypeList().size());
//    }
}