package proj.zav.jra.observable;

import proj.zav.jra.observer.Observer;

import java.util.EventListener;

public interface Observable <Q extends EventListener, T extends Observer<Q>> {
    boolean addObserver(T observer);
    boolean removeObserver(T observer);
    void notifyObserver(T observer);
    void notifyAllObservers();
}
