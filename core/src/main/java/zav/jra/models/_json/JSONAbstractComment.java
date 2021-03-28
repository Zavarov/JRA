package zav.jra.models._json;

import org.json.JSONObject;
import zav.jra.models.AbstractComment;
import zav.jra.models.Kind;
import zav.jra.models.Listing;
import zav.jra.models.Thing;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.function.Supplier;

public class JSONAbstractComment extends JSONAbstractCommentTOP{
    public static <T extends AbstractComment> T fromThing(String source, Supplier<T> supplier){
        return fromThing(JSONThing.fromJson(source), supplier);
    }

    public static <T extends AbstractComment> T fromThing(Thing source, Supplier<T> supplier){
        assert source.getKind() == Kind.Comment;

        T target = supplier.get();
        fromJson(target, source.getData().toString());
        return target;
    }

    @Override
    protected void $fromCreatedUtc(JSONObject source, AbstractComment target){
        double seconds = source.getDouble(CREATEDUTC);
        Instant instant = Instant.ofEpochSecond((long)seconds);
        OffsetDateTime date = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        target.setCreatedUtc(date);
    }

    @Override
    protected void $toCreatedUtc(AbstractComment source, JSONObject target){
        double seconds = source.toEpochSecondCreatedUtc();
        target.put(CREATEDUTC, seconds);
    }

    @Override
    protected void $fromEdited(JSONObject source, AbstractComment target){
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
    protected void $toEdited(AbstractComment source, JSONObject target){
        source.ifPresentEdited(edited -> target.put(EDITED, edited.toEpochSecond()));
    }

    @Override
    protected void $fromReplies(JSONObject source, AbstractComment target){
        //In case a comment doesn't have replies, an empty string is returned
        JSONObject value = source.optJSONObject(REPLIES);

        if(value != null)
            JSONListing.fromThing(value).forEach(target::addReplies);
    }

    @Override
    protected void $toReplies(AbstractComment source, JSONObject target){
        if(!source.isEmptyReplies()){
            Listing<Thing> listing = new Listing<>(JSONThing::fromJson);

            for(Thing reply : source.getReplies())
                listing.addChildren(JSONThing.toJson(reply, new JSONObject()).toString());

            Thing thing = listing.toThing();

            target.put(REPLIES, JSONThing.toJson(thing, new JSONObject()));
        }
    }
}
