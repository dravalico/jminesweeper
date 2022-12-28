package it.units.sdm.jminesweeper;

import java.awt.*;
import java.util.Map;
import java.util.Random;

public class MinesPlacer {
    private static final Random RANDOM = new Random();

    private enum Movement {
        DIAGONAL,
        VERTICAL,
        HORIZONTAL;

        public static Movement getMovementFromInt(int value) {
            if (value == 0) {
                return DIAGONAL;
            }
            if (value == 1) {
                return VERTICAL;
            } else {
                return HORIZONTAL;
            }
        }
    }

    private record Pair<F, S>(F first, S second) {
    }

    private MinesPlacer() {
    }

    public static void place(Map<Point, TileValue> board, int minesNumber, Point firstClickPosition) {
        for (int i = 0; i < minesNumber; i++) {
            Point minePosition = computeMineablePosition(board, firstClickPosition);
            board.put(minePosition, new TileValue(GameSymbol.MINE));
        }
    }

    private static Point computeMineablePosition(Map<Point, TileValue> board, Point firstClickPosition) {
        Dimension dimension = BoardUtil.computeBoardDimension(board);
        int standardDeviation = 1;
        Point startingPoint = generateRandomPointWithin(dimension);
        Point minePosition = new Point(startingPoint.x, startingPoint.y);
        while (board.get(minePosition).isAMine() || isInNeighborhood(minePosition, firstClickPosition)) {
            standardDeviation = standardDeviation * 2;
            Pair<Integer, Integer> shifts = computeRandomShifts(standardDeviation, dimension);
            minePosition.x = shiftCoordinateFromStartingPoint(shifts.first, startingPoint.x, dimension.width);
            minePosition.y = shiftCoordinateFromStartingPoint(shifts.second, startingPoint.y, dimension.height);
        }
        return minePosition;
    }

    private static Point generateRandomPointWithin(Dimension dimensionLimit) {
        return new Point(RANDOM.nextInt(dimensionLimit.width), RANDOM.nextInt(dimensionLimit.height));
    }

    private static boolean isInNeighborhood(Point point, Point startingPoint) {
        return point.distance(startingPoint) <= Math.sqrt(2);
    }

    private static Pair<Integer, Integer> computeRandomShifts(int standardDeviation, Dimension dimension) {
        int moveWhere = RANDOM.nextInt(Movement.values().length);
        int xTranslation = 0;
        int yTranslation = 0;
        switch (Movement.getMovementFromInt(moveWhere)) {
            case DIAGONAL -> {
                xTranslation = sampleShift(standardDeviation, dimension.width);
                yTranslation = sampleShift(standardDeviation, dimension.height);
            }
            case VERTICAL -> yTranslation = sampleShift(standardDeviation, dimension.height);
            case HORIZONTAL -> xTranslation = sampleShift(standardDeviation, dimension.width);
        }
        return new Pair<>(xTranslation, yTranslation);
    }

    private static int sampleShift(int standardDeviation, double dimension) {
        double sample = RANDOM.nextGaussian() * standardDeviation;
        return (int) shiftInBoardDimensionality(sample, dimension);
    }

    private static double shiftInBoardDimensionality(double x, double dimension) {
        return dimension * (Math.atan(x) / (Math.PI) / 2);
    }

    private static int shiftCoordinateFromStartingPoint(int shift, int startingPoint, int sideDimension) {
        int finalCoordinate;
        if (shift > 0) {
            finalCoordinate = startingPoint + shift < sideDimension ? startingPoint + shift : sideDimension - shift - 1;
        } else {
            finalCoordinate = startingPoint + shift >= 0 ? startingPoint + shift : sideDimension + startingPoint + shift;
        }
        return finalCoordinate;
    }

}
