package vartas.jra.models._json;

import com.google.common.collect.ImmutableBiMap;
import org.json.JSONObject;
import vartas.jra.models.Kind;
import vartas.jra.models.Thing;

public class JSONThing extends JSONThingTOP{
    private static final ImmutableBiMap<String, Kind> NAMES = new ImmutableBiMap.Builder<String, Kind>()
                .put("t1",Kind.Comment)
                .put("t2",Kind.Account)
                .put("t3",Kind.Link)
                .put("t4",Kind.Message)
                .put("t5",Kind.Subreddit)
                .put("t6",Kind.Award)
                .put("Listing",Kind.Listing)
                .put("More",Kind.More)
                .put("UserList",Kind.UserList)
                .put("TrophyList",Kind.TrophyList)
                .put("KarmaList",Kind.KarmaList)
                .put("subreddit_settings",Kind.SubredditSettings)
                .build();

    public static Thing fromJson(String source){
        return fromJson(new JSONObject(source));
    }

    public static Thing fromJson(JSONObject source){
        return fromJson(new Thing(), source);
    }

    @Override
    protected void $fromData(JSONObject source, Thing target){
        target.setData(source.get(DATA));
    }

    @Override
    protected void $toData(Thing source, JSONObject target){
        target.put(DATA, source.getData());
    }

    @Override
    protected void $fromKind(JSONObject source, Thing target) {
        target.setKind(NAMES.getOrDefault(source.getString(KIND), Kind.Unknown));
    }

    @Override
    protected void $toKind(Thing source, JSONObject target) {
        target.put(KIND, NAMES.inverse().get(source.getKind()));
    }
}
