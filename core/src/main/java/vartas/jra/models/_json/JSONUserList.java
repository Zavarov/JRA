package vartas.jra.models._json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.jra.Client;
import vartas.jra.models.UserList;
import vartas.jra.models._factory.UserListFactory;

public class JSONUserList extends JSONUserListTOP{
    public static final String KEY = "children";

    public static UserList fromJson(Client client, String source){
        return fromJson(client, new JSONObject(source));
    }

    public static UserList fromJson(Client client, JSONObject source){
        return fromJson(UserListFactory.create(client), source);
    }

    @Override
    protected void $fromData(JSONObject source, UserList target){
        JSONArray values = source.getJSONArray(KEY);
        for(int i = 0 ; i < values.length() ; ++i)
            target.addData(JSONFakeAccount.fromJson(target.getClient(), values.getJSONObject(i)));
    }

    @Override
    protected void $toData(UserList source, JSONObject target){
        JSONArray values = new JSONArray();
        source.streamData().map(user -> JSONFakeAccount.toJson(user, new JSONObject())).forEach(values::put);
        target.put(KEY, values);
    }
}
