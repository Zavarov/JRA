package proj.zav.jra.models._json;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import proj.zav.jra.models.AbstractAccount;
import proj.zav.jra.models.Kind;
import proj.zav.jra.models.Thing;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Supplier;

public class JSONAbstractAccount extends JSONAbstractAccountTOP{

    public static <T extends AbstractAccount> T fromThing(String source, Supplier<T> supplier){
        Thing thing = JSONThing.fromJson(source);

        assert thing.getKind() == Kind.Account;

        T target = supplier.get();
        fromJson(target, thing.getData().toString());
        return target;
    }

    @Override
    protected void $fromCreatedUtc(JSONObject source, AbstractAccount target){
        double seconds = source.getDouble(CREATEDUTC);
        Instant instant = Instant.ofEpochSecond((long)seconds);
        OffsetDateTime date = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        target.setCreatedUtc(date);
    }

    @Override
    protected void $toCreatedUtc(AbstractAccount source, JSONObject target){
        double seconds = source.toEpochSecondCreatedUtc();
        target.put(CREATEDUTC, seconds);
    }

    @Override
    protected void $fromIconImage(JSONObject source, AbstractAccount target){
        target.setIconImage(StringEscapeUtils.unescapeHtml4(source.getString(ICONIMAGE)));
    }
}
