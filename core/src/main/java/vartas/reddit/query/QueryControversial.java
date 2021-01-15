package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.types.Thing;

import java.util.function.Function;

public class QueryControversial<T> extends QuerySort<T,QueryControversial<T>>{

    public QueryControversial(Function<Thing, T> transformer, Client client, Endpoint endpoint, Object... args) {
        super(transformer, client, endpoint, args);
    }

    @Override
    protected QueryControversial<T> getRealThis() {
        return this;
    }
}
