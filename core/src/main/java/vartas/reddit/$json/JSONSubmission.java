package vartas.reddit.$json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.reddit.Submission;

import java.time.Instant;

public class JSONSubmission extends JSONSubmissionTOP{
    private static final String ROOT_COMMENTS = "comments";
    private static final String CREATED = "created";

    @Override
    protected void $fromRootComments(JSONObject source, Submission target) {
        JSONArray rootComments = source.optJSONArray(ROOT_COMMENTS);

        if(rootComments != null)
            for(int i = 0 ; i < rootComments.length() ; ++i)
                target.addRootComments(JSONComment.fromJson(new $JSONComment(target), rootComments.getJSONObject(i)));
    }

    @Override
    protected void $toRootComments(Submission source, JSONObject target) {
        JSONArray rootComments = new JSONArray();

        source.forEachRootComments(rootComment -> rootComments.put(JSONComment.toJson(rootComment, new JSONObject())));

        if(!rootComments.isEmpty())
            target.put(ROOT_COMMENTS, rootComments);
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
