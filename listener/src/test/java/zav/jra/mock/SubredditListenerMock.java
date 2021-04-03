package zav.jra.mock;

import zav.jra.Link;
import zav.jra.Subreddit;
import zav.jra.listener.SubredditListener;

import javax.annotation.Nonnull;

public class SubredditListenerMock implements SubredditListener {
    @Override
    public void newLink(@Nonnull Subreddit subreddit, @Nonnull Link link) {
        System.out.println("\t"+link.getTitle());
    }
}
