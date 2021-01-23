package vartas.jra.query.listings;

import vartas.jra.Client;
import vartas.jra.Endpoint;
import vartas.jra.query.QueryMany;
import vartas.jra.types.Thing;

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
