package vartas.jra.models._json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.jra.models.Thing;
import vartas.jra.models.Trophy;
import vartas.jra.models.TrophyList;
import vartas.jra.models._factory.ThingFactory;

public class JSONTrophyList extends JSONTrophyListTOP{
    public static final String KEY = "trophies";
    @Override
    protected void $fromData(JSONObject source, TrophyList target){
        JSONArray values = source.getJSONArray(KEY);
        for(int i = 0 ; i < values.length() ; ++i){
            Thing thing = Thing.from(values.getJSONObject(i));
            target.addData(thing.toTrophy());
        }
    }

    @Override
    protected void $toData(TrophyList source, JSONObject target){
        JSONArray values = new JSONArray();
        for(Trophy trophy : source.getData()){
            Thing thing = ThingFactory.create(Thing.Kind.Award.toString(), JSONTrophy.toJson(trophy, new JSONObject()));
            values.put(JSONThing.toJson(thing, new JSONObject()));
        }
        target.put(KEY, values);
    }
}