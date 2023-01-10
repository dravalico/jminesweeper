package it.units.sdm.jminesweeper.core;

@FunctionalInterface
public interface ActionHandler<A, R> {

    R actionAt(A action);

}
