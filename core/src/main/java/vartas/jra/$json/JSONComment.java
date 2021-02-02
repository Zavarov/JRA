package vartas.jra.$json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.jra.Comment;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public class JSONComment extends JSONCommentTOP{
    @Override
    protected void $fromCreatedUtc(JSONObject source, Comment target){
        double seconds = source.getDouble(CREATEDUTC);
        Instant instant = Instant.ofEpochSecond((long)seconds);
        OffsetDateTime date = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        target.setCreatedUtc(date);
    }

    @Override
    protected void $toCreatedUtc(Comment source, JSONObject target){
        double seconds = source.toEpochSecondCreatedUtc();
        target.put(CREATEDUTC, seconds);
    }

    @Override
    protected void $fromEdited(JSONObject source, Comment target){
        //Returns false if not edited, occasionally true for very old comments
        if(source.get(EDITED) instanceof Boolean) {
            target.setEdited(Optional.empty());
        }else{
            double seconds = source.getDouble(EDITED);
            Instant instant = Instant.ofEpochSecond((long)seconds);
            OffsetDateTime date = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
            target.setEdited(date);
        }
    }

    @Override
    protected void $toEdited(Comment source, JSONObject target){
        source.ifPresentEdited(edited -> target.put(EDITED, edited.toEpochSecond()));
    }

    @Override
    protected void $fromReplies(JSONObject source, Comment target){
        JSONArray values = source.optJSONArray(REPLIES);
        if(values != null){
            for(int i = 0 ; i < values.length() ; ++i){
                target.addReplies(JSONComment.fromJson(new Comment(), values.getJSONObject(i)));
            }
        }
    }

    @Override
    protected void $toReplies(Comment source, JSONObject target){
        if(!source.isEmptyReplies()){
            JSONArray values = new JSONArray();
            source.forEachReplies(comment -> values.put(JSONComment.toJson(comment, new JSONObject())));
            target.put(REPLIES, values);
        }
    }
}
