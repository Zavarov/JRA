package vartas.reddit.query.listings;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.query.QueryMany;
import vartas.reddit.types.Thing;

import java.util.function.Function;

public class QueryRising <T> extends QueryMany<T,QueryRising<T>> {
    public QueryRising(Function<Thing, T> mapper, Client client, Endpoint endpoint, Object... args) {
        super(mapper, client, endpoint, args);
    }

    @Override
    protected QueryRising<T> getRealThis() {
        return this;
    }
}
