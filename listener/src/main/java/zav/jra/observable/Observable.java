package zav.jra.observable;

import zav.jra.observer.Observer;

import java.io.IOException;
import java.util.EventListener;

public interface Observable <Q extends EventListener, T extends Observer<Q>> {
    boolean addObserver(T observer);
    boolean removeObserver(T observer);
    void notifyObserver(T observer) throws IOException;
    void notifyAllObservers() throws IOException;
    int size();
}
