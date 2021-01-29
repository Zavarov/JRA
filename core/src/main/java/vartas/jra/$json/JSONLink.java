package vartas.jra.$json;

import org.json.JSONObject;
import vartas.jra.Link;

public class JSONLink extends JSONLinkTOP{
    @Override
    protected void $fromSource(JSONObject source, Link target){
        target.setSource(source.getJSONObject(SOURCE));
    }

    @Override
    protected void $toSource(Link source, JSONObject target){
        target.put(SOURCE, source.getSource());
    }
}