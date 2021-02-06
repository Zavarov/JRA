package vartas.jra._json;

import org.json.JSONObject;
import vartas.jra.Comment;
import vartas.jra.models.Listing;
import vartas.jra.models.Thing;
import vartas.jra.models._factory.ListingFactory;
import vartas.jra.models._json.JSONThing;

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
        //May not existing after serialization
        if(source.isNull(EDITED) || source.get(EDITED) instanceof Boolean) {
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
        //In case a comment doesn't have replies, an empty string is returned
        JSONObject value = source.optJSONObject(REPLIES);


        if(value != null) {
            Thing thing = Thing.from(value);
            Listing listing = thing.toListing();
            listing.streamChildren().map(Thing::from).map(Thing::toComment).forEach(target::addReplies);
        }
    }

    @Override
    protected void $toReplies(Comment source, JSONObject target){
        if(!source.isEmptyReplies()){
            Listing listing = ListingFactory.create();

            for(Comment reply : source.getReplies())
                listing.addChildren(JSONThing.toJson(reply.toThing(), new JSONObject()).toString());

            Thing thing = listing.toThing();

            target.put(REPLIES, JSONThing.toJson(thing, new JSONObject()));
        }
    }
}
