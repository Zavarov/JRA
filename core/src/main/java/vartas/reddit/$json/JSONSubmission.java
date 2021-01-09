package vartas.reddit.$json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.reddit.Submission;

import java.time.Instant;

public class JSONSubmission extends JSONSubmissionTOP{
    private static final String THUMBNAIL = "thumbnail";
    private static final String LINK_FLAIR_TEXT = "linkFlairText";
    private static final String CONTENT = "content";
    private static final String COMMENTS = "comments";
    private static final String CREATED = "created";

    @Override
    protected void $fromComments(JSONObject source, Submission target) {
        JSONArray comments = source.optJSONArray(COMMENTS);

        if(comments != null)
            for(int i = 0 ; i < comments.length() ; ++i)
                target.addComments(JSONComment.fromJson(new $JSONComment(target), comments.getJSONObject(i)));
    }

    @Override
    protected void $toComments(Submission source, JSONObject target) {
        JSONArray comments = new JSONArray();

        source.forEachComments(comment -> comments.put(JSONComment.toJson(comment, new JSONObject())));

        if(!comments.isEmpty())
            target.put(COMMENTS, comments);
    }

    @Override
    protected void $fromCreated(JSONObject source, Submission target) {
        target.setCreated(Instant.ofEpochSecond(source.getLong(CREATED)));
    }

    @Override
    protected void $toCreated(Submission source, JSONObject target) {
        target.put(CREATED, source.getCreated().toEpochMilli() / 1000);
    }
}
