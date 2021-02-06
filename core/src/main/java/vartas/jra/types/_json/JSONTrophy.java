package vartas.jra.types._json;

import org.json.JSONObject;
import vartas.jra.types.Trophy;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class JSONTrophy extends JSONTrophyTOP{
    private static final String GRANTED_AT = "granted_at";
    @Override
    protected void $fromGrantedAt(JSONObject source, Trophy target){
        if(source.isNull(GRANTED_AT))
            return;

        double seconds = source.getDouble(GRANTED_AT);
        Instant instant = Instant.ofEpochSecond((long)seconds);
        OffsetDateTime date = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        target.setGrantedAt(date);
    }

    @Override
    protected void $toGrantedAt(Trophy source, JSONObject target){
        if(source.isEmptyGrantedAt())
            return;

        double seconds = source.orElseThrowGrantedAt().toEpochSecond();
        target.put(GRANTED_AT, seconds);
    }
}
