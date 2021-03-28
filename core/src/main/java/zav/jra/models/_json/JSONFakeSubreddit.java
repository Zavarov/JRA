package zav.jra.models._json;

import org.json.JSONObject;
import zav.jra.models.FakeSubreddit;

public class JSONFakeSubreddit extends JSONFakeSubredditTOP{

    @Override
    protected void $fromData(JSONObject source, FakeSubreddit target){
        target.setData(source.toMap());
    }

    @Override
    protected void $toData(FakeSubreddit source, JSONObject target){
        source.forEachData(target::put);
    }
}
