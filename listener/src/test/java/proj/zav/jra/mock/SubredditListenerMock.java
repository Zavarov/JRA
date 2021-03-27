package proj.zav.jra.mock;

import org.jetbrains.annotations.NotNull;
import proj.zav.jra.Link;
import proj.zav.jra.listener.SubredditListener;

public class SubredditListenerMock implements SubredditListener {
    @Override
    public void newLink(@NotNull Link link) {
        System.out.println("\t"+link.getTitle());
    }
}
