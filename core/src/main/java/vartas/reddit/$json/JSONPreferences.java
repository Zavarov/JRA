package vartas.reddit.$json;

import org.json.JSONObject;
import vartas.reddit.Preferences;

public class JSONPreferences extends JSONPreferencesTOP{
    @Override
    protected void $fromDefaultThemeSubreddit(JSONObject source, Preferences target){
        target.setDefaultThemeSubreddit(source.get(DEFAULTTHEMESUBREDDIT));
    }

    @Override
    protected void $toDefaultThemeSubreddit(Preferences source, JSONObject target){
        target.put(DEFAULTTHEMESUBREDDIT, source.getDefaultThemeSubreddit());
    }
}
