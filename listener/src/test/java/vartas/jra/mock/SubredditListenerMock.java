package vartas.jra.mock;

import org.jetbrains.annotations.NotNull;
import vartas.jra.Link;
import vartas.jra.listener.SubredditListener;

public class SubredditListenerMock implements SubredditListener {
    @Override
    public void newLink(@NotNull Link link) {
        System.out.println("\t"+link.getTitle());
    }
}
