package vartas.jra.types._json;

import org.json.JSONObject;
import vartas.jra.types.UserData;
import vartas.jra.types.UserDataMap;

public class JSONUserDataMap extends JSONUserDataMapTOP{
    @Override
    protected void $fromUserData(JSONObject source, UserDataMap target){
        for(String key : source.keySet())
            target.putUserData(key, JSONUserData.fromJson(new UserData(), source.getJSONObject(key)));
    }

    @Override
    protected void $toUserData(UserDataMap source, JSONObject target){
        for(String key : source.keySetUserData())
            target.put(key, JSONUserData.toJson(source.getUserData(key), new JSONObject()));
    }
}
