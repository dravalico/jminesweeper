package it.units.sdm.jminesweeper.core;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.core.generation.BoardInitializer;
import it.units.sdm.jminesweeper.core.generation.MinesPlacer;
import it.units.sdm.jminesweeper.enumeration.GameSymbol;
import it.units.sdm.jminesweeper.event.*;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class GameManager extends AbstractBoard<Map<Point, Tile>> implements ActionHandler<Point> {
    private final GameConfiguration gameConfiguration;
    private final BoardInitializer boardInitializer;
    private int uncoveredTiles;
    private final Map<EventType, List<GameEventListener>> listenersMap;
    private boolean isGameFinished;

    public GameManager(GameConfiguration gameConfiguration, MinesPlacer<Map<Point, Tile>, Point> minesPlacer) {
        super(new LinkedHashMap<>());
        this.gameConfiguration = gameConfiguration;
        boardInitializer = new BoardInitializer(gameConfiguration, minesPlacer);
        boardInitializer.fillBoard(board);
        uncoveredTiles = 0;
        listenersMap = new EnumMap<>(EventType.class);
        isGameFinished = false;
    }

    public Map<Point, GameSymbol> getBoardStatus() {
        return board.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().isCovered() ? GameSymbol.COVERED : e.getValue().getValue()));
    }

    @Override
    public void addListener(GameEventListener listener, EventType... eventTypes) {
        Arrays.stream(eventTypes).forEach(e -> {
            listenersMap.putIfAbsent(e, new ArrayList<>());
            listenersMap.get(e).add(listener);
        });
    }

    @Override
    public void notifyListeners(GameEvent event) {
        listenersMap.getOrDefault(event.getEventType(), new ArrayList<>())
                .forEach(l -> l.onGameEvent(event));
    }

    @Override
    public void actionAt(Point point) {
        verifyPointWithinBoardDimension(point);
        if (uncoveredTiles == 0) {
            boardInitializer.init(board, point);
        }
        if (!isGameFinished) {
            uncoverTriggeredTiles(point);
        }
        if (isDefeat()) {
            notifyListeners(new DefeatEvent(this));
            isGameFinished = true;
            return;
        }
        if (isVictory()) {
            notifyListeners(new VictoryEvent(this));
            isGameFinished = true;
            return;
        }
        notifyListeners(new ProgressEvent(this));
    }

    private void verifyPointWithinBoardDimension(Point point) {
        Dimension boardDimension = gameConfiguration.dimension();
        if (((point.x < 0) || (point.x >= boardDimension.height)) || ((point.y < 0) || (point.y >= boardDimension.width))) {
            throw new IllegalArgumentException("Coordinates not allowed!");
        }
    }

    private void uncoverFreeSpotRecursively(Point point) {
        uncoverTile(point);
        Dimension dimension = gameConfiguration.dimension();
        int iStart = (point.x == 0 ? 0 : -1);
        int iStop = (point.x == dimension.height - 1 ? 0 : 1);
        int jStart = (point.y == 0 ? 0 : -1);
        int jStop = (point.y == dimension.width - 1 ? 0 : 1);
        for (int i = iStart; i <= iStop; i++) {
            for (int j = jStart; j <= jStop; j++) {
                Point temp = new Point(point.x + i, point.y + j);
                if (board.get(temp).isCovered()) {
                    if (board.get(temp).isNumber()) {
                        uncoverTile(temp);
                    } else {
                        uncoverFreeSpotRecursively(temp);
                    }
                }
            }
        }
    }

    private void uncoverTriggeredTiles(Point point) {
        if (!board.get(point).isCovered()) {
            return;
        }
        if (board.get(point).isNumber() || board.get(point).isMine()) {
            uncoverTile(point);
        } else {
            uncoverFreeSpotRecursively(point);
        }
    }

    private void uncoverTile(Point point) {
        if (board.get(point).isCovered()) {
            uncoveredTiles = uncoveredTiles + 1;
            board.get(point).uncover();
        }
    }

    private boolean isDefeat() {
        return board.values()
                .stream()
                .anyMatch(v -> v.isMine() && !v.isCovered());
    }

    private boolean isVictory() {
        return (board.size() - gameConfiguration.minesNumber()) == uncoveredTiles;
    }

}
