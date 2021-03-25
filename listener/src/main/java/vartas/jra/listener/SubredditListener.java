package vartas.jra.listener;

import vartas.jra.Link;

import javax.annotation.Nonnull;
import java.util.EventListener;

public interface SubredditListener extends EventListener {
    default void newLink(@Nonnull Link link){
    }
}
