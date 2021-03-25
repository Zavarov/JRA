package net.zav.jra.mock;

import net.zav.jra.models.AbstractSubreddit;
import net.zav.jra.models.Kind;
import net.zav.jra.models.Thing;
import net.zav.jra.models._json.JSONAbstractSubreddit;

public class SubredditMock extends AbstractSubreddit {
    public static SubredditMock from(Thing thing){
        assert thing.getKind() == Kind.Subreddit;

        SubredditMock target = new SubredditMock();
        JSONAbstractSubreddit.fromJson(target, thing.getData().toString());
        return target;
    }
}
