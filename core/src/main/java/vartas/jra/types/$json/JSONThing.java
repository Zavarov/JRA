package vartas.jra.types.$json;

import org.json.JSONObject;
import vartas.jra.types.Thing;

public class JSONThing extends JSONThingTOP{
    @Override
    protected void $fromData(JSONObject source, Thing target){
        target.setData(source.get(DATA));
    }

    @Override
    protected void $toData(Thing source, JSONObject target){
        target.put(DATA, source.getData());
    }
}
