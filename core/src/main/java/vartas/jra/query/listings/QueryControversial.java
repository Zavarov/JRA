package vartas.jra.query.listings;

import vartas.jra.Client;
import vartas.jra.Endpoint;
import vartas.jra.query.QuerySort;
import vartas.jra.types.Thing;

import java.util.function.Function;

public class QueryControversial<T> extends QuerySort<T,QueryControversial<T>> {

    public QueryControversial(Function<Thing, T> transformer, Client client, Endpoint endpoint, Object... args) {
        super(transformer, client, endpoint, args);
    }

    @Override
    protected QueryControversial<T> getRealThis() {
        return this;
    }
}
