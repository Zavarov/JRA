package vartas.reddit.types.$json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.reddit.types.User;
import vartas.reddit.types.UserList;

public class JSONUserList extends JSONUserListTOP{
    public static final String KEY = "children";
    @Override
    protected void $fromData(JSONObject source, UserList target){
        JSONArray values = source.getJSONArray(KEY);
        for(int i = 0 ; i < values.length() ; ++i)
            target.addData(JSONUser.fromJson(new User(), values.getJSONObject(i)));
    }

    @Override
    protected void $toData(UserList source, JSONObject target){
        JSONArray values = new JSONArray();
        source.streamData().map(user -> JSONUser.toJson(user, new JSONObject())).forEach(values::put);
        target.put(KEY, values);
    }
}
