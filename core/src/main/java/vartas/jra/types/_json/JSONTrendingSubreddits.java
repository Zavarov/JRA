package vartas.jra.types._json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.jra.types.TrendingSubreddits;

public class JSONTrendingSubreddits extends JSONTrendingSubredditsTOP{
    protected void $fromSubredditNames(JSONObject source, TrendingSubreddits target){
        JSONArray data = source.optJSONArray(SUBREDDITNAMES);
        if(data != null)
            for(int i = 0 ; i < data.length() ; ++i)
                target.addSubredditNames(data.getString(i));
    }
    protected void $toSubredditNames(TrendingSubreddits source, JSONObject target){
        JSONArray data = new JSONArray();
        if(!source.isEmptySubredditNames()) {
            source.forEachSubredditNames(data::put);
            target.put(SUBREDDITNAMES, data);
        }
    }
}
