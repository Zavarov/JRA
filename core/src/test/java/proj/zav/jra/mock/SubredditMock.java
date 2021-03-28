package zav.jra.mock;

import zav.jra.models.AbstractSubreddit;
import zav.jra.models.Kind;
import zav.jra.models.Thing;
import zav.jra.models._json.JSONAbstractSubreddit;

public class SubredditMock extends AbstractSubreddit {
    public static SubredditMock from(Thing thing){
        assert thing.getKind() == Kind.Subreddit;

        SubredditMock target = new SubredditMock();
        JSONAbstractSubreddit.fromJson(target, thing.getData().toString());
        return target;
    }
}
