package proj.zav.jra.models._json;

import org.json.JSONObject;
import proj.zav.jra.models.Kind;
import proj.zav.jra.models.Thing;
import proj.zav.jra.models.Trophy;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class JSONTrophy extends JSONTrophyTOP{

    public static Trophy fromThing(String source){
        return fromThing(new JSONObject(source));
    }

    public static Trophy fromThing(JSONObject source){
        Thing thing = JSONThing.fromJson(source);

        assert thing.getKind() == Kind.Award;

        return fromJson(new Trophy(), thing.getData().toString());
    }

    @Override
    protected void $fromGrantedAt(JSONObject source, Trophy target){
        if(!source.isNull(GRANTEDAT)) {
            double seconds = source.getDouble(GRANTEDAT);
            Instant instant = Instant.ofEpochSecond((long) seconds);
            OffsetDateTime date = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
            target.setGrantedAt(date);
        }
    }

    @Override
    protected void $toGrantedAt(Trophy source, JSONObject target){
        if(!source.isEmptyGrantedAt()) {
            double seconds = source.orElseThrowGrantedAt().toEpochSecond();
            target.put(GRANTEDAT, seconds);
        }
    }
}
