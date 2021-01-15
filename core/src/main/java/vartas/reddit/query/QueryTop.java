package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.types.Thing;

import java.util.function.Function;

public class QueryTop<T> extends QuerySort<T,QueryTop<T>>{
    public QueryTop(Function<Thing, T> transformer, Client client, Endpoint endpoint, Object... args) {
        super(transformer, client, endpoint, args);
    }

    @Override
    protected QueryTop<T> getRealThis() {
        return this;
    }
}
