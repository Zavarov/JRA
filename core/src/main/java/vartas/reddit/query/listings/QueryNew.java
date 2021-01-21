package vartas.reddit.query.listings;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.query.QueryMany;
import vartas.reddit.types.Thing;

import java.util.function.Function;

public class QueryNew<T> extends QueryMany<T, QueryNew<T>> {
    public QueryNew(Function<Thing, T> mapper, Client client, Endpoint endpoint, Object... args) {
        super(mapper, client, endpoint, args);
    }

    @Override
    protected QueryNew<T> getRealThis() {
        return this;
    }
}
