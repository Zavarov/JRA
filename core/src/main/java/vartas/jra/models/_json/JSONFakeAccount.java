package vartas.jra.models._json;

import org.json.JSONObject;
import vartas.jra.Client;
import vartas.jra.models.FakeAccount;
import vartas.jra.models._factory.FakeAccountFactory;

public class JSONFakeAccount extends JSONFakeAccountTOP{

    public static FakeAccount fromJson(Client client, String source){
        return fromJson(client, new JSONObject(source));
    }

    public static FakeAccount fromJson(Client client, JSONObject data){
        return fromJson(
                FakeAccountFactory.create(FakeAccount::new, client, data.getString("name")),
                data
        );
    }

    @Override
    protected void $fromData(JSONObject source, FakeAccount target){
        target.setData(source.toMap());
    }

    @Override
    protected void $toData(FakeAccount source, JSONObject target){
        source.forEachData(target::put);
    }
}
