package zav.jra.models._json;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.json.JSONObject;
import zav.jra.models.AbstractLink;
import zav.jra.models.Kind;
import zav.jra.models.Thing;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.function.Supplier;

public class JSONAbstractLink extends JSONAbstractLinkTOP{
    public static <T extends AbstractLink> T fromThing(String source, Supplier<T> supplier){
        return fromThing(JSONThing.fromJson(source), supplier);
    }

    public static <T extends AbstractLink> T fromThing(Thing source, Supplier<T> supplier){
        assert source.getKind() == Kind.Comment;

        T target = supplier.get();
        fromJson(target, source.getData().toString());
        return target;
    }

    @Override
    protected void $fromThumbnail(JSONObject source, AbstractLink target){
        //Bug #5 - Link thumbnails may be "self"
        //Bug #8 - Link thumbnails may be "default"
        String thumbnail = source.optString(THUMBNAIL, null);
        UrlValidator validator = new UrlValidator();

        if(thumbnail != null && validator.isValid(thumbnail))
            target.setThumbnail(thumbnail);
    }

    @Override
    protected void $fromCreatedUtc(JSONObject source, AbstractLink target){
        double seconds = source.getDouble(CREATEDUTC);
        Instant instant = Instant.ofEpochSecond((long)seconds);
        OffsetDateTime date = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        target.setCreatedUtc(date);
    }

    @Override
    protected void $toCreatedUtc(AbstractLink source, JSONObject target){
        double seconds = source.toEpochSecondCreatedUtc();
        target.put(CREATEDUTC, seconds);
    }

    @Override
    protected void $fromEdited(JSONObject source, AbstractLink target){
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
    protected void $toEdited(AbstractLink source, JSONObject target){
        source.ifPresentEdited(edited -> target.put(EDITED, edited.toEpochSecond()));
    }

    @Override
    protected void $fromMedia(JSONObject source, AbstractLink target){
        if(!source.isNull(MEDIA))
            target.setMedia(source.get(MEDIA));
    }

    @Override
    protected void $toMedia(AbstractLink source, JSONObject target){
        source.ifPresentMedia(media -> target.put(MEDIA, media));
    }

    @Override
    protected void $fromMediaEmbed(JSONObject source, AbstractLink target){
        target.setMediaEmbed(source.get(MEDIAEMBED));
    }

    @Override
    protected void $toMediaEmbed(AbstractLink source, JSONObject target){
        target.put(MEDIAEMBED, source.getMediaEmbed());
    }

    @Override
    protected void $fromSelftextHtml(JSONObject source, AbstractLink target) {
        String value = source.optString(SELFTEXTHTML);

        if(!value.isBlank())
            target.setSelftextHtml(StringEscapeUtils.unescapeHtml4(value));
    }

    @Override
    protected void $fromTitle(JSONObject source, AbstractLink target) {
        target.setTitle(StringEscapeUtils.unescapeHtml4(source.getString(TITLE)));
    }
}
