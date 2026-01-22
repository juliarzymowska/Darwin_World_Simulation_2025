package agh.ics.oop.simulationGUI;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.map.EarthMap;
import agh.ics.oop.model.map.FeromonMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.Vector2d;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Map;

/*
 * Map Renderer is responsible for rendering the WorldMap onto a JavaFX Canvas.
 * It handles scaling, centering, and drawing various map layers such as
 * preferred fields, pheromones, grid lines, plants, and animals.
 * */
public class MapRenderer {
    private final Canvas canvas;
    private final Color[] pheromoneColors = new Color[20];

    private double cellSize, offsetX, offsetY;
    private double mapWidth, mapHeight;

    public MapRenderer(Canvas canvas) {
        this.canvas = canvas;
        initializeColors();
    }

    private void initializeColors() {
        // to reduce complexity
        for (int i = 0; i < 20; i++) {
            double opacity = Math.min(i * 0.15, 0.6);
            pheromoneColors[i] = Color.rgb(0, 0, 255, opacity);
        }
    }

    /*
     * Computes clicked position coordinates
     * */
    public Vector2d getClickPosition(double mouseX, double mouseY) {
        double mapX = mouseX - offsetX;
        double mapY = mouseY - offsetY;

        if (mapX < 0 || mapX >= mapWidth * cellSize || mapY < 0 || mapY >= mapHeight * cellSize) {
            return null;
        }

        int col = (int) (mapX / cellSize);
        int row = (int) (mapHeight - (mapY / cellSize));

        if (row >= mapHeight) row = (int) mapHeight - 1;
        if (row < 0) row = 0;

        return new Vector2d(col, row);
    }

    public void draw(WorldMap worldMap, RenderContext context) {
        if (worldMap == null) return;

        this.mapWidth = worldMap.getCurrentBounds().rightUpMapCorner().getX() + 1;
        this.mapHeight = worldMap.getCurrentBounds().rightUpMapCorner().getY() + 1;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        double canvasW = canvas.getWidth();
        double canvasH = canvas.getHeight();

        gc.clearRect(0, 0, canvasW, canvasH);

        // Calculate Scale
        double sizeX = canvasW / mapWidth;
        double sizeY = canvasH / mapHeight;
        this.cellSize = Math.min(sizeX, sizeY);

        this.offsetX = (canvasW - mapWidth * cellSize) / 2;
        this.offsetY = (canvasH - mapHeight * cellSize) / 2;

        // Background
        gc.setFill(Color.rgb(221, 229, 182));
        gc.fillRect(offsetX, offsetY, mapWidth * cellSize, mapHeight * cellSize);

        // Layers
        if (context.showPreferredFields()) drawPreferredFields(gc, worldMap); // firstly draw preferred fields, bcs
        // they are under everything
        drawPheromones(gc, worldMap);
        drawGrid(gc);
        drawObjects(gc, worldMap, context);
    }

    /*
     * Based on EarthMap preferred fields - jungle area
     * */
    private void drawPreferredFields(GraphicsContext gc, WorldMap worldMap) {
        if (!(worldMap instanceof EarthMap map)) return;
        gc.setFill(Color.rgb(247, 244, 230));

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (map.isPreferredPosition(new Vector2d(x, y))) {
                    drawRect(gc, x, y, cellSize);
                }
            }
        }
    }

    /*
     * Pheromones drawing with opacity based on intensity of smell
     * */
    private void drawPheromones(GraphicsContext gc, WorldMap worldMap) {
        if (worldMap instanceof FeromonMap feromonMap) {
            Map<Vector2d, Integer> activeSmells = feromonMap.getActiveFeromons();
            for (Map.Entry<Vector2d, Integer> entry : activeSmells.entrySet()) {
                Vector2d pos = entry.getKey();
                int intensity = entry.getValue();

                if (isInsideBounds(pos)) {
                    int index = Math.min(intensity, 19);
                    gc.setFill(pheromoneColors[index]);
                    drawRect(gc, pos.getX(), pos.getY(), cellSize);
                }
            }
        }
    }

    /*
     * Grid to improve visibility
     * */
    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.rgb(173, 193, 120));
        gc.setLineWidth(Math.max(0.5, cellSize * 0.05));

        double gridWidth = mapWidth * cellSize;
        double gridHeight = mapHeight * cellSize;

        for (int x = 0; x <= mapWidth; x++) {
            double xPos = offsetX + (x * cellSize);
            gc.strokeLine(xPos, offsetY, xPos, offsetY + gridHeight);
        }
        for (int y = 0; y <= mapHeight; y++) {
            double yPos = offsetY + (y * cellSize);
            gc.strokeLine(offsetX, yPos, offsetX + gridWidth, yPos);
        }
    }

    private void drawObjects(GraphicsContext gc, WorldMap worldMap, RenderContext context) {
        if (!(worldMap instanceof EarthMap map)) return;

        // Plants
        gc.setFill(Color.rgb(102, 109, 74));
        for (Plant plant : map.getElementsManager().getPlants()) {
            if (plant != null && plant.getCurrentPosition() != null && isInsideBounds(plant.getCurrentPosition())) {
                drawCircle(gc, plant.getCurrentPosition().getX(), plant.getCurrentPosition().getY(), 0.6);
            }
        }

        // Animals
        for (Animal animal : map.getElementsManager().getAnimals()) {
            if (animal == null || animal.getCurrentPosition() == null || !isInsideBounds(animal.getCurrentPosition()))
                continue;

            Vector2d pos = animal.getCurrentPosition();
            gc.setFill(getEnergyColor(animal));
            drawCircle(gc, pos.getX(), pos.getY(), 1.0);

            // Highlights
            if (animal.equals(context.trackedAnimal())) { // tracked animal
                gc.setStroke(Color.rgb(251, 111, 146));
                gc.setLineWidth(Math.max(1.5, cellSize * 0.1));
                drawCircleOutline(gc, pos.getX(), pos.getY(), 1.0);
            } else if (context.showDominantGenotype() && context.topGenotypes().contains(animal.getGenotype())) { // dominant genotype
                gc.setStroke(Color.rgb(255, 215, 0));
                gc.setLineWidth(Math.max(1.0, cellSize * 0.08));
                drawCircleOutline(gc, pos.getX(), pos.getY(), 1.0);
            }
        }
    }

    // Helper to draw relative to map coordinates
    private void drawRect(GraphicsContext gc, int x, int y, double size) {
        double drawX = offsetX + x * cellSize;
        double drawY = offsetY + (mapHeight - 1 - y) * cellSize;
        gc.fillRect(drawX, drawY, size, size);
    }

    private void drawCircle(GraphicsContext gc, int x, int y, double scale) {
        double drawX = offsetX + (x * cellSize);
        double drawY = offsetY + ((mapHeight - 1 - y) * cellSize);
        double size = cellSize * scale;
        double offset = (cellSize - size) / 2;
        gc.fillOval(drawX + offset, drawY + offset, size, size);
    }

    private void drawCircleOutline(GraphicsContext gc, int x, int y, double scale) {
        double drawX = offsetX + (x * cellSize);
        double drawY = offsetY + ((mapHeight - 1 - y) * cellSize);
        gc.strokeOval(drawX, drawY, cellSize, cellSize);
    }

    private boolean isInsideBounds(Vector2d pos) {
        return pos.getX() >= 0 && pos.getX() < mapWidth && pos.getY() >= 0 && pos.getY() < mapHeight;
    }

    // helper method for drawing animals based on their energy level
    // it's hue from 0 (red) to 240 (blue)
    private Color getEnergyColor(Animal animal) {
        int energy = animal.getEnergy();
        double hue = (double) (energy * 240) / animal.getMaxEnergy();
        return Color.hsb(hue, 1, 1);
    }
}