package vartas.jra.types.$json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.jra.types.Listing;
import vartas.jra.types.Thing;

import java.util.List;

public class JSONListing extends JSONListingTOP{
    @Override
    protected void $fromChildren(JSONObject source, Listing target) {
        JSONArray data = source.optJSONArray(CHILDREN);
        if(data != null){
            for(int i = 0 ; i < data.length() ; ++i)
                target.addChildren(JSONThing.fromJson(new Thing(), data.getJSONObject(i)));
        }
    }
    @Override
    protected void $toChildren(Listing source, JSONObject target) {
        List<Thing> data = source.getChildren();
        if(!data.isEmpty()){
            JSONArray array = new JSONArray();
            for(Thing thing : data)
                array.put(JSONThing.toJson(thing, new JSONObject()));
            target.put(CHILDREN, array);
        }
    }
}
