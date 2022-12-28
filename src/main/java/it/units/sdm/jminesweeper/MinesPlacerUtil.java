package it.units.sdm.jminesweeper;

import java.awt.*;
import java.util.Map;
import java.util.Random;

public class MinesPlacerUtil {
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

    private MinesPlacerUtil() {
    }

    public static void placeMines(Map<Point, TileValue> board, int minesNumber, Point firstClickPosition) {
        for (int i = 0; i < minesNumber; i++) {
            board.put(computeAvailablePointUsingGaussianRandomVariables(board, firstClickPosition), new TileValue(GameSymbol.MINE));
        }
    }

    private static Point computeAvailablePointUsingGaussianRandomVariables(Map<Point, TileValue> board, Point firstClickPosition) {
        Dimension dimension = BoardUtil.computeBoardDimension(board);
        int standardDeviation = 1;
        Random random = new Random();
        Point startingPoint = new Point(random.nextInt(dimension.width), random.nextInt(dimension.height));
        Point minePosition = new Point(startingPoint.x, startingPoint.y);
        boolean isInCoordinatesNeighborhood = firstClickPosition.distance(minePosition) <= Math.sqrt(2);
        while (board.get(minePosition).isAMine() || isInCoordinatesNeighborhood) {
            standardDeviation = standardDeviation * 2;
            int moveWhere = random.nextInt(Movement.values().length);
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
            minePosition.x = shiftCoordinateFromStartingPoint(xTranslation, startingPoint.x, dimension.width);
            minePosition.y = shiftCoordinateFromStartingPoint(yTranslation, startingPoint.y, dimension.height);
            isInCoordinatesNeighborhood = firstClickPosition.distance(minePosition) <= Math.sqrt(2);
        }
        return minePosition;
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

    private static int sampleShift(int standardDeviation, double dimension) {
        Random random = new Random();
        double sample = random.nextGaussian() * standardDeviation;
        return (int) (dimension * (Math.atan(sample) / (Math.PI) / 2));
    }

}
