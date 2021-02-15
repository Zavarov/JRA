package vartas.jra.models._json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.jra.models.FakeAccount;
import vartas.jra.models.Kind;
import vartas.jra.models.Thing;
import vartas.jra.models.UserList;

public class JSONUserList extends JSONUserListTOP{
    public static final String KEY = "children";

    public static UserList fromThing(String source){
        return fromThing(new JSONObject(source));
    }

    public static UserList fromThing(JSONObject source){
        Thing thing = JSONThing.fromJson(source);

        assert thing.getKind() == Kind.UserList;

        return fromJson(new UserList(), thing.getData().toString());
    }

    @Override
    protected void $fromData(JSONObject source, UserList target){
        JSONArray node = source.optJSONArray(KEY);

        if(node != null) {
            for (int i = 0; i < node.length(); ++i)
                target.addData(JSONFakeAccount.fromJson(new FakeAccount(), node.getJSONObject(i)));
        }
    }

    @Override
    protected void $toData(UserList source, JSONObject target){
        JSONArray node = new JSONArray();

        if(!source.isEmptyData()) {
            for (FakeAccount data : source.getData())
                node.put(JSONFakeAccount.toJson(data, new JSONObject()));
            target.put(KEY, node);
        }
    }
}
