package vartas.reddit.$json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.reddit.Submission;

import java.time.Instant;
import java.util.Optional;

public class JSONSubmission extends JSONSubmissionTOP{
    private static final String THUMBNAIL = "thumbnail";
    private static final String LINK_FLAIR_TEXT = "linkFlairText";
    private static final String CONTENT = "content";
    private static final String ROOT_COMMENTS = "comments";
    private static final String CREATED = "created";

    @Override
    protected void $fromThumbnail(JSONObject source, Submission target) {
        target.setThumbnail(Optional.ofNullable(source.optString(THUMBNAIL, null)));
    }

    @Override
    protected void $toThumbnail(Submission source, JSONObject target) {
        source.getThumbnail().ifPresent(value -> target.put(THUMBNAIL, value));
    }

    @Override
    protected void $fromLinkFlairText(JSONObject source, Submission target) {
        target.setLinkFlairText(Optional.ofNullable(source.optString(LINK_FLAIR_TEXT, null)));
    }

    @Override
    protected void $toLinkFlairText(Submission source, JSONObject target) {
        source.getLinkFlairText().ifPresent(value -> target.put(LINK_FLAIR_TEXT, value));
    }

    @Override
    protected void $fromContent(JSONObject source, Submission target) {
        target.setContent(Optional.ofNullable(source.optString(CONTENT, null)));
    }

    @Override
    protected void $toContent(Submission source, JSONObject target) {
        source.getContent().ifPresent(value -> target.put(CONTENT, value));
    }

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
