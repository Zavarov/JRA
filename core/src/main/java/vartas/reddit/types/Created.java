package vartas.reddit.types;

import java.time.*;

public interface Created extends CreatedTOP{
    String CREATED = "created";
    String CREATED_UTC = "created_utc";

    @Override
    default LocalDateTime getCreated() {
        Instant created = Instant.ofEpochSecond(getSource().getLong(CREATED));
        return LocalDateTime.ofInstant(created, ZoneId.systemDefault());
    }

    @Override
    default OffsetDateTime getCreatedUtc() {
        Instant created = Instant.ofEpochSecond(getSource().getLong(CREATED_UTC));
        return OffsetDateTime.ofInstant(created, ZoneOffset.UTC);
    }


    default Created getRealThis(){
        return this;
    }
}
