package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.types.Thing;

import java.util.function.Function;

public class QueryNew<T> extends QueryThing<T, QueryNew<T>> {
    public QueryNew(Function<Thing, T> transformer, Client client, Endpoint endpoint, Object... args) {
        super(transformer, client, endpoint, args);
    }

    @Override
    protected QueryNew<T> getRealThis() {
        return this;
    }
}
