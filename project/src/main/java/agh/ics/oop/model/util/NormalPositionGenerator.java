package agh.ics.oop.model.util;

import java.util.*;


public class NormalPositionGenerator implements Iterable<Vector2d>{
    private final int n;
    private final int x;
    private final int y;

    public NormalPositionGenerator(int n, int x, int y) {
        this.n = n;
        this.x = x;
        this.y = y;
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new RandomPositionIterator(n,x,y);
    }

    private static class RandomPositionIterator implements Iterator<Vector2d> {
        private final int n;
        private int current;
        private final int x_range;
        private final int y_range;
        private final Set<Vector2d> used_positions = new HashSet<>();
        private final Random rand;

        public RandomPositionIterator(int n, int x, int y) {
            this.rand = new Random();
            this.n = n;
            this.x_range = x;
            this.y_range = y;
            this.current = 0;
        }

        @Override
        public boolean hasNext() {
            return current < n;
        }

        @Override
        public Vector2d next() {
            if (!hasNext()){
                throw new NoSuchElementException();
            }
            int y;
            double meanY = 0.5*y_range;
            double sigmaY = 0.09 *y_range;
            do {
                y = (int) Math.round(meanY + rand.nextGaussian() * sigmaY);
            } while (y > y_range || y < 0);
            int x = rand.nextInt(x_range);
            Vector2d pos = new Vector2d(x, y);
            if (!used_positions.contains(pos)){
                used_positions.add(pos);
                current++;
            }
            return pos;
        }
    }
}