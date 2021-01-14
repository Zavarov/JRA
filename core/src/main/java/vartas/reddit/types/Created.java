package vartas.reddit.types;

import java.time.Instant;

public interface Created extends CreatedTOP{
    String CREATED = "created";
    String CREATED_UTC = "created_utc";

    @Override
    default Instant getCreated() {
        return Instant.ofEpochSecond(getSource().getLong(CREATED));
    }

    @Override
    default Instant getCreatedUtc() {
        return Instant.ofEpochSecond(getSource().getLong(CREATED_UTC));
    }


    default Created getRealThis(){
        return this;
    }
}
