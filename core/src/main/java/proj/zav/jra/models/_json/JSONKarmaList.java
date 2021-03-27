package proj.zav.jra.models._json;

import org.json.JSONArray;
import org.json.JSONObject;
import proj.zav.jra.models.Karma;
import proj.zav.jra.models.KarmaList;
import proj.zav.jra.models.Kind;
import proj.zav.jra.models.Thing;

public class JSONKarmaList extends JSONKarmaListTOP{
    public static final String KEY = "karmas";

    public static KarmaList fromThing(String source){
        Thing thing = JSONThing.fromJson(source);

        assert thing.getKind() == Kind.KarmaList;

        //For some reason, they decided to store the Karma entries directly inside an array, instead of wrapping it
        // around a JSONObject. We create such a dummy to make it compatible with the deserialization functions.
        JSONObject root = new JSONObject();
        root.put(KEY, new JSONArray(thing.getData().toString()));
        return fromJson(new KarmaList(), root);
    }

    @Override
    protected void $fromData(JSONObject source, KarmaList target){
        JSONArray node = source.optJSONArray(KEY);

        if(node != null) {
            for (int i = 0; i < node.length(); ++i)
                target.addData(JSONKarma.fromJson(new Karma(), node.getJSONObject(i)));
        }
    }

    @Override
    protected void $toData(KarmaList source, JSONObject target){
        JSONArray node = new JSONArray();

        if(!source.isEmptyData()) {
            for (Karma data : source.getData())
                node.put(JSONKarma.toJson(data, new JSONObject()));
            target.put(KEY, node);
        }
    }
}
