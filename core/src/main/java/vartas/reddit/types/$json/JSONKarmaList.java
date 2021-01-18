package vartas.reddit.types.$json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.reddit.types.Karma;
import vartas.reddit.types.KarmaList;

public class JSONKarmaList extends JSONKarmaListTOP{
    public static final String KEY = "children";
    @Override
    protected void $fromData(JSONObject source, KarmaList target){
        JSONArray node = source.getJSONArray(KEY);
        for(int i = 0 ; i < node.length() ; ++i)
            target.addData(JSONKarma.fromJson(new Karma(), node.getJSONObject(i)));
    }

    @Override
    protected void $toData(KarmaList source, JSONObject target){
        JSONArray node = new JSONArray();
        for(Karma data : source.getData())
            node.put(data);
        target.put(KEY, node);
    }
}
