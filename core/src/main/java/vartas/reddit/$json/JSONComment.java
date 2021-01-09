package vartas.reddit.$json;

import org.json.JSONObject;
import vartas.reddit.Comment;

import java.time.Instant;

public class JSONComment extends JSONCommentTOP{
    private static final String CREATED = "created";

    @Override
    protected void $fromCreated(JSONObject source, Comment target) {
        target.setCreated(Instant.ofEpochSecond(source.getLong(CREATED)));
    }

    @Override
    protected void $toCreated(Comment source, JSONObject target) {
        target.put(CREATED, source.getCreated().toEpochMilli() / 1000);
    }
}
