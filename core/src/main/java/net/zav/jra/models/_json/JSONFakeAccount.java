package net.zav.jra.models._json;

import net.zav.jra.models.FakeAccount;
import org.json.JSONObject;

public class JSONFakeAccount extends JSONFakeAccountTOP{
    public static FakeAccount fromJson(String source){
        return fromJson(new FakeAccount(), source);
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
