package zav.jra.observer;

import java.io.IOException;
import java.util.EventListener;

public interface Observer <T extends EventListener> {
    boolean addListener(T listener);
    boolean removeListener(T listener);
    void notifyListener(T listener) throws IOException;
    void notifyAllListener() throws  IOException;
    int size();
}
