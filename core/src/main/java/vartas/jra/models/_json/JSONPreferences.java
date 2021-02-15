package vartas.jra.models._json;

import org.json.JSONObject;
import vartas.jra.models.Preferences;

public class JSONPreferences extends JSONPreferencesTOP{
    public static Preferences fromJson(String source){
        return fromJson(new Preferences(), source);
    }

    @Override
    protected void $fromDefaultThemeSubreddit(JSONObject source, Preferences target){
        if(!source.isNull(DEFAULTTHEMESUBREDDIT))
            target.setDefaultThemeSubreddit(source.get(DEFAULTTHEMESUBREDDIT));
    }

    @Override
    protected void $toDefaultThemeSubreddit(Preferences source, JSONObject target){
        if(source.isPresentDefaultThemeSubreddit())
            target.put(DEFAULTTHEMESUBREDDIT, source.orElseThrowDefaultThemeSubreddit());
    }
}
