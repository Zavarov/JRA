package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.types.Thing;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class QueryHot<T> extends QueryMany<T,QueryHot<T>> {
    protected static final String REGION = "g";
    public QueryHot(Function<Thing, T> mapper, Client client, Endpoint endpoint, Object... args) {
        super(mapper, client, endpoint, args);
    }

    @Override
    protected QueryHot<T> getRealThis() {
        return this;
    }

    public QueryHot<T> setRegion(@Nonnull GeoLocation geoLocation){
        params.put(REGION, geoLocation);
        return this;
    }
}
