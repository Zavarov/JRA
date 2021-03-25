package net.zav.jra.models._json;

import net.zav.jra.models.Kind;
import net.zav.jra.models.Listing;
import net.zav.jra.models.Thing;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.function.Function;

public class JSONListing extends JSONListingTOP{
    public static Listing<Thing> fromThing(String source){
        return fromThing(new JSONObject(source));
    }

    public static Listing<Thing> fromThing(JSONObject source){
        return fromThing(source, Function.identity());
    }

    public static <T> Listing<T> fromThing(String source, Function<Thing, T> mapper){
        return fromThing(new JSONObject(source), mapper);
    }

    public static <T> Listing<T> fromThing(JSONObject source, Function<Thing, T> mapper){
        return fromJson(source, value -> mapper.apply(JSONThing.fromJson(value)));
    }

    public static <T> Listing<T> fromJson(String source, Function<String, T> mapper){
        return fromJson(new JSONObject(source), mapper);
    }

    public static <T> Listing<T> fromJson(JSONObject source, Function<String, T> mapper){
        Thing thing = JSONThing.fromJson(source);

        assert thing.getKind() == Kind.Listing;

        Listing<T> target = new Listing<>(mapper);
        JSONListing.fromJson(target, thing.getData().toString());
        return target;
    }

    //-----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void $fromChildren(JSONObject source, Listing target) {
        JSONArray data = source.optJSONArray(CHILDREN);

        if(data != null){
            for(int i = 0 ; i < data.length() ; ++i)
                target.addChildren(data.get(i));
        }
    }

    @Override
    protected void $toChildren(Listing source, JSONObject target) {
        List<Object> data = source.getChildren();
        if(!data.isEmpty()){
            JSONArray array = new JSONArray();
            for(Object thing : data)
                array.put(thing);
            target.put(CHILDREN, array);
        }
    }
}
