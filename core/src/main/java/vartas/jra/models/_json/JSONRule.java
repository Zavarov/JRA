package vartas.jra.models._json;

import org.json.JSONObject;
import vartas.jra.models.Rule;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class JSONRule extends JSONRuleTOP{
    @Override
    protected void $fromCreatedUtc(JSONObject source, Rule target){
        double seconds = source.getDouble(CREATEDUTC);
        Instant instant = Instant.ofEpochSecond((long)seconds);
        target.setCreatedUtc(OffsetDateTime.ofInstant(instant, ZoneOffset.UTC));
    }

    @Override
    protected void $toCreatedUtc(Rule source, JSONObject target){
        target.put(CREATEDUTC, source.getCreatedUtc().toEpochSecond());
    }
}
