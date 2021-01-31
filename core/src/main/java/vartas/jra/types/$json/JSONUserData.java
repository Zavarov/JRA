package vartas.jra.types.$json;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import vartas.jra.types.UserData;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class JSONUserData extends JSONUserDataTOP{
    @Override
    protected void $fromCreatedUtc(JSONObject source, UserData target){
        double seconds = source.getDouble(CREATEDUTC);
        Instant instant = Instant.ofEpochSecond((long)seconds);
        OffsetDateTime date = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        target.setCreatedUtc(date);
    }

    @Override
    protected void $toCreatedUtc(UserData source, JSONObject target){
        double seconds = source.toEpochSecondCreatedUtc();
        target.put(CREATEDUTC, seconds);
    }

    @Override
    protected void $fromProfileImage(JSONObject source, UserData target){
        target.setProfileImage(StringEscapeUtils.unescapeHtml4(source.getString(PROFILEIMAGE)));
    }
}
