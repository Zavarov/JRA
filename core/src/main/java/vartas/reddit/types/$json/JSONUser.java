package vartas.reddit.types.$json;

import org.json.JSONObject;
import vartas.reddit.types.User;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class JSONUser extends JSONUserTOP{
    private static final String DATE = "date";
    @Override
    protected void $fromDate(JSONObject source, User target){
        double seconds = source.getDouble(DATE);
        Instant instant = Instant.ofEpochSecond((long)seconds);
        OffsetDateTime date = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        target.setDate(date);
    }

    @Override
    protected void $toDate(User source, JSONObject target){
        double seconds = source.toEpochSecondDate();
        target.put(DATE, seconds);
    }
}
