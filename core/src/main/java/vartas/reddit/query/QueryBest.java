package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.types.Thing;

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
