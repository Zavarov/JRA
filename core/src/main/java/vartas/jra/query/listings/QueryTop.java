package vartas.jra.query.listings;

import vartas.jra.Client;
import vartas.jra.Endpoint;
import vartas.jra.query.QuerySort;
import vartas.jra.types.Thing;

import java.util.function.Function;

public class QueryTop<T> extends QuerySort<T,QueryTop<T>> {
    public QueryTop(Function<Thing, T> transformer, Client client, Endpoint endpoint, Object... args) {
        super(transformer, client, endpoint, args);
    }

    @Override
    protected QueryTop<T> getRealThis() {
        return this;
    }
}
