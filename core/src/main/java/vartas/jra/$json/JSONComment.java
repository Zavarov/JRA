package vartas.jra.$json;

import org.json.JSONObject;
import vartas.jra.Comment;

public class JSONComment extends JSONCommentTOP{
    @Override
    protected void $fromSource(JSONObject source, Comment target){
        target.setSource(source.getJSONObject(SOURCE));
    }

    @Override
    protected void $toSource(Comment source, JSONObject target){
        target.put(SOURCE, source.getSource());
    }
}
