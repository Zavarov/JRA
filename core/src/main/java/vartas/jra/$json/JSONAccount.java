package vartas.jra.$json;

import org.json.JSONObject;
import vartas.jra.Account;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class JSONAccount extends JSONAccountTOP{
    @Override
    protected void $fromCreatedUtc(JSONObject source, Account target){
        double seconds = source.getDouble(CREATEDUTC);
        Instant instant = Instant.ofEpochSecond((long)seconds);
        OffsetDateTime date = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        target.setCreatedUtc(date);
    }

    @Override
    protected void $toCreatedUtc(Account source, JSONObject target){
        double seconds = source.toEpochSecondCreatedUtc();
        target.put(CREATEDUTC, seconds);
    }
}
