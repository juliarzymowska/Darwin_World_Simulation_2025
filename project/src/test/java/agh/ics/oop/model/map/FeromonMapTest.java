package agh.ics.oop.model.map;

import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Feromon;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeromonMapTest {
    private FeromonMap feromonMap;

    private ConfigMap createDefaultConfig(double moveToFeromonProbability, int daysToDecreaseFeromon, int smellRange) {
        return new ConfigMap(10, 10, 0, 0, MapType.FEROMON_MAP, moveToFeromonProbability, daysToDecreaseFeromon, smellRange);
    }

    @BeforeEach
    void setUp() {
        feromonMap = new FeromonMap(createDefaultConfig(1.0,1,2));
    }


    @Test
    void addFeromonIncreasesValue() {
        Vector2d pos = new Vector2d(2, 2);

        feromonMap.addFeromon(pos);
        Feromon f = (Feromon) feromonMap.objectAt(pos);
        int valAfterFirst = f.getFeromonValue();

        feromonMap.addFeromon(pos);
        assertEquals(valAfterFirst + 1, f.getFeromonValue(), "Wartość powinna wzrosnąć po ponownym dodaniu");
    }

    @Test
    void decreaseFeromonsRemovesFromMap() {
        Vector2d pos = new Vector2d(1, 1);
        feromonMap.addFeromon(pos);

        Feromon f = (Feromon) feromonMap.objectAt(pos);
        int stepsToDie = f.getFeromonValue();

        for (int i = 0; i < stepsToDie; i++) {
            assertNotNull(feromonMap.objectAt(pos));
            feromonMap.decreaseFeromons();
        }

        assertNull(feromonMap.objectAt(pos), "Feromon powinien zostać usunięty przy wartości 0");
    }

    @Test
    void moveToFeromon() {
        Vector2d animalPos = new Vector2d(0, 0);
        Vector2d feromonPos = new Vector2d(1, 1);

        Animal animal = new Animal(animalPos);
        feromonMap.placeAnimal(animal);
        feromonMap.addFeromon(feromonPos);

        feromonMap.moveTo(animal);

        assertEquals(new Vector2d(1, 1), animal.getCurrentPosition(), "Zwierzę powinno pójść w stronę feromonu");
    }

    @Test
    void objectAtPriority() {
        Vector2d pos = new Vector2d(5, 5);
        Animal animal = new Animal(pos);
        feromonMap.placeAnimal(animal);
        feromonMap.addFeromon(pos);

        Object target = feromonMap.objectAt(pos);

        assertInstanceOf(Animal.class, target, "Zwierzę powinno mieć priorytet wyświetlania nad feromonem");
    }
}