package vartas.jra.observable;

import vartas.jra.observer.Observer;

public interface Observable <T extends Observer> {
    boolean addObserver(T observer);
    boolean removeObserver(T observer);
    void notifyObserver(T observer);
    void notifyAllObservers();
}
