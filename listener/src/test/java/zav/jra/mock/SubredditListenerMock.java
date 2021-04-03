package zav.jra.mock;

import org.jetbrains.annotations.NotNull;
import zav.jra.Link;
import zav.jra.listener.SubredditListener;

public class SubredditListenerMock implements SubredditListener {
    @Override
    public void newLink(@NotNull Link link) {
        System.out.println("\t"+link.getTitle());
    }
}
