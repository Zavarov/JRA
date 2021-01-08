package vartas.reddit.$json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.reddit.Comment;

import java.time.Instant;

public class JSONComment extends JSONCommentTOP{
    private static final String CHILDREN = "children";
    private static final String CREATED = "created";

    @Override
    protected void $fromChildren(JSONObject source, Comment target) {
        JSONArray children = source.optJSONArray(CHILDREN);

        if(children != null)
            for(int i = 0 ; i < children.length() ; ++i)
                target.addChildren(fromJson(new $JSONComment(target.getSubmission()), children.getJSONObject(i)));
    }

    @Override
    protected void $toChildren(Comment source, JSONObject target) {
        JSONArray children = new JSONArray();

        source.forEachChildren(child -> children.put(toJson(child, new JSONObject())));

        if(!children.isEmpty())
            target.put(CHILDREN, children);
    }

    @Override
    protected void $fromCreated(JSONObject source, Comment target) {
        target.setCreated(Instant.ofEpochSecond(source.getLong(CREATED)));
    }

    @Override
    protected void $toCreated(Comment source, JSONObject target) {
        target.put(CREATED, source.getCreated().toEpochMilli() / 1000);
    }
}
