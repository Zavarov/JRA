package vartas.jra.types._json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.jra.types.Listing;

import java.util.List;

public class JSONListing extends JSONListingTOP{
    @Override
    protected void $fromChildren(JSONObject source, Listing target) {
        JSONArray data = source.optJSONArray(CHILDREN);
        if(data != null){
            for(int i = 0 ; i < data.length() ; ++i)
                target.addChildren(data.get(i).toString());
        }
    }
    @Override
    protected void $toChildren(Listing source, JSONObject target) {
        List<String> data = source.getChildren();
        if(!data.isEmpty()){
            JSONArray array = new JSONArray();
            for(String thing : data)
                array.put(thing);
            target.put(CHILDREN, array);
        }
    }
}
