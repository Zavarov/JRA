package proj.zav.jra.mock;

import proj.zav.jra.models.AbstractSubreddit;
import proj.zav.jra.models.Kind;
import proj.zav.jra.models.Thing;
import proj.zav.jra.models._json.JSONAbstractSubreddit;

public class SubredditMock extends AbstractSubreddit {
    public static SubredditMock from(Thing thing){
        assert thing.getKind() == Kind.Subreddit;

        SubredditMock target = new SubredditMock();
        JSONAbstractSubreddit.fromJson(target, thing.getData().toString());
        return target;
    }
}
