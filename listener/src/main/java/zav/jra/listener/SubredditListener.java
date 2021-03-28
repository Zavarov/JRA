package zav.jra.listener;

import zav.jra.Link;

import javax.annotation.Nonnull;
import java.util.EventListener;

public interface SubredditListener extends EventListener {
    default void newLink(@Nonnull Link link){
    }
}
