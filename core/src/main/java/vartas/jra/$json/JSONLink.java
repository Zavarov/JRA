package vartas.jra.$json;

import org.json.JSONObject;
import vartas.jra.Link;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public class JSONLink extends JSONLinkTOP{
    @Override
    protected void $fromCreatedUtc(JSONObject source, Link target){
        double seconds = source.getDouble(CREATEDUTC);
        Instant instant = Instant.ofEpochSecond((long)seconds);
        OffsetDateTime date = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        target.setCreatedUtc(date);
    }

    @Override
    protected void $toCreatedUtc(Link source, JSONObject target){
        double seconds = source.toEpochSecondCreatedUtc();
        target.put(CREATEDUTC, seconds);
    }

    @Override
    protected void $fromEdited(JSONObject source, Link target){
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
    protected void $toEdited(Link source, JSONObject target){
        source.ifPresentEdited(edited -> target.put(EDITED, edited.toEpochSecond()));
    }

    @Override
    protected void $fromMedia(JSONObject source, Link target){
        target.setMedia(source.get(MEDIA));
    }

    @Override
    protected void $toMedia(Link source, JSONObject target){
        target.put(MEDIA, source.getMedia());
    }

    @Override
    protected void $fromMediaEmbed(JSONObject source, Link target){
        target.setMedia(source.get(MEDIAEMBED));
    }

    @Override
    protected void $toMediaEmbed(Link source, JSONObject target){
        target.put(MEDIAEMBED, source.getMedia());
    }
}
