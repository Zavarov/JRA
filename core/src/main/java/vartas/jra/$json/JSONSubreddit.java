package vartas.jra.$json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.jra.Subreddit;

import java.util.List;

public class JSONSubreddit extends JSONSubredditTOP{
    @Override
    protected void $fromHeaderSize(JSONObject source, Subreddit target){
        JSONArray bounds = source.optJSONArray(HEADERSIZE);
        if(bounds != null) {
            for(int i = 0 ; i < bounds.length() ; ++i)
                target.addHeaderSize(bounds.getInt(i));
        }
    }

    @Override
    protected void $toHeaderSize(Subreddit source, JSONObject target){
        List<Integer> bounds = source.getHeaderSize();
        if(!bounds.isEmpty())
            target.put(HEADERSIZE, new JSONArray(bounds));
    }
}
