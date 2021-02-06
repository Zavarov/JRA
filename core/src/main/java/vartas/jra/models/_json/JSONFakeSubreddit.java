package vartas.jra.models._json;

import org.json.JSONObject;
import vartas.jra.Client;
import vartas.jra.models.FakeSubreddit;
import vartas.jra.models._factory.FakeSubredditFactory;

public class JSONFakeSubreddit extends JSONFakeSubredditTOP{

    public static FakeSubreddit fromJson(Client client, JSONObject data){
        return fromJson(
                FakeSubredditFactory.create(FakeSubreddit::new, client, data.getString("name")),
                data
        );
    }

    @Override
    protected void $fromData(JSONObject source, FakeSubreddit target){
        target.setData(source.toMap());
    }

    @Override
    protected void $toData(FakeSubreddit source, JSONObject target){
        source.forEachData(target::put);
    }
}
