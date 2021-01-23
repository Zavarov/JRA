package vartas.jra.query.listings;

import vartas.jra.Client;
import vartas.jra.Endpoint;
import vartas.jra.query.QueryMany;
import vartas.jra.types.Thing;

import java.util.function.Function;

public class QueryBest<T> extends QueryMany<T,QueryBest<T>> {

    public QueryBest(Function<Thing, T> mapper, Client client, Endpoint endpoint, Object... args) {
        super(mapper, client, endpoint, args);
    }

    @Override
    protected QueryBest<T> getRealThis() {
        return this;
    }
}
