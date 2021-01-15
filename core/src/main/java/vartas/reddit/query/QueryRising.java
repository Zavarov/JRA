package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.types.Thing;

import java.util.function.Function;

public class QueryRising <T> extends QueryThing<T,QueryRising<T>> {
    public QueryRising(Function<Thing, T> transformer, Client client, Endpoint endpoint, Object... args) {
        super(transformer, client, endpoint, args);
    }

    @Override
    protected QueryRising<T> getRealThis() {
        return this;
    }
}
