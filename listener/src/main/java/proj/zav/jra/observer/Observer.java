package proj.zav.jra.observer;

import java.util.EventListener;

public interface Observer <T extends EventListener> {
    boolean addListener(T listener);
    boolean removeListener(T listener);
    void notifyListener(T listener);
    void notifyAllListener();
}
