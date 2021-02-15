package vartas.jra.mock;

import vartas.jra.models.AbstractSubreddit;
import vartas.jra.models.Kind;
import vartas.jra.models.Thing;
import vartas.jra.models._json.JSONAbstractSubreddit;

public class SubredditMock extends AbstractSubreddit {
    public static SubredditMock from(Thing thing){
        assert thing.getKind() == Kind.Subreddit;

        SubredditMock target = new SubredditMock();
        JSONAbstractSubreddit.fromJson(target, thing.getData().toString());
        return target;
    }
}
