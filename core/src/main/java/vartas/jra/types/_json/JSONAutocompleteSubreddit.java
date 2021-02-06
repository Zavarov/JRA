package vartas.jra.types._json;

import org.json.JSONObject;
import vartas.jra.types.AutocompleteSubreddit;

public class JSONAutocompleteSubreddit extends JSONAutocompleteSubredditTOP{
    @Override
    protected void $fromAllowedPostTypes(JSONObject source, AutocompleteSubreddit target){
        JSONObject node = source.optJSONObject(ALLOWEDPOSTTYPES);

        if(node != null)
            for(String key : node.keySet())
                target.putAllowedPostTypes(key, node.getBoolean(key));
    }

    @Override
    protected void $toAllowedPostTypes(AutocompleteSubreddit source, JSONObject target){
        if(source.isEmptyAllowedPostTypes())
            return;

        JSONObject node = new JSONObject();

        source.forEachAllowedPostTypes(node::put);

        target.put(ALLOWEDPOSTTYPES, node);
    }
}
