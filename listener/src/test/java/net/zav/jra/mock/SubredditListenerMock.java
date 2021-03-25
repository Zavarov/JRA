package net.zav.jra.mock;

import net.zav.jra.Link;
import net.zav.jra.listener.SubredditListener;
import org.jetbrains.annotations.NotNull;

public class SubredditListenerMock implements SubredditListener {
    @Override
    public void newLink(@NotNull Link link) {
        System.out.println("\t"+link.getTitle());
    }
}
