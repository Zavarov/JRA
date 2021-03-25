package net.zav.jra.models._json;

import net.zav.jra.models.FakeSubreddit;
import org.json.JSONObject;

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
