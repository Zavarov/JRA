package vartas.jra.types._json;

import org.json.JSONObject;
import vartas.jra.types.SearchSubreddit;

import java.awt.*;

public class JSONSearchSubreddit extends JSONSearchSubredditTOP{
    @Override
    protected void $fromKeyColor(JSONObject source, SearchSubreddit target){
        if(source.optString(KEYCOLOR).isBlank())
            return;

        target.setKeyColor(Color.decode(source.getString(KEYCOLOR)));
    }

    @Override
    protected void $toKeyColor(SearchSubreddit source, JSONObject target){
        if(source.isEmptyKeyColor())
            return;

        Color color = source.orElseThrowKeyColor();

        target.put(KEYCOLOR, String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue()));
    }
}
