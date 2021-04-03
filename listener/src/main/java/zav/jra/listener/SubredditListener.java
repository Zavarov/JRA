package zav.jra.listener;

import zav.jra.Link;
import zav.jra.Subreddit;

import javax.annotation.Nonnull;
import java.util.EventListener;

public interface SubredditListener extends EventListener {
    default void newLink(@Nonnull Subreddit subreddit, @Nonnull Link link){
    }
}
