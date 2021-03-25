package net.zav.jra.models._json;

import net.zav.jra.models.AbstractPreferences;
import org.json.JSONObject;

import java.util.function.Supplier;

public class JSONAbstractPreferences extends JSONAbstractPreferencesTOP{
    public static <T extends AbstractPreferences> T fromJson(String source, Supplier<T> supplier){
        T target = supplier.get();
        fromJson(target, source);
        return target;
    }

    @Override
    protected void $fromDefaultThemeSubreddit(JSONObject source, AbstractPreferences target){
        if(!source.isNull(DEFAULTTHEMESUBREDDIT))
            target.setDefaultThemeSubreddit(source.get(DEFAULTTHEMESUBREDDIT));
    }

    @Override
    protected void $toDefaultThemeSubreddit(AbstractPreferences source, JSONObject target){
        if(source.isPresentDefaultThemeSubreddit())
            target.put(DEFAULTTHEMESUBREDDIT, source.orElseThrowDefaultThemeSubreddit());
    }
}
