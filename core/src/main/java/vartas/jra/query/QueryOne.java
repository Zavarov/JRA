package vartas.jra.query;

import vartas.jra.Client;
import vartas.jra.Endpoint;

import java.util.function.Function;

public class QueryOne<T> extends QueryGet<T, QueryOne<T>>{
    public QueryOne(Function<String, T> mapper, Client client, Endpoint endpoint, Object... args) {
        super(mapper, client, endpoint, args);
    }

    @Override
    protected QueryOne<T> getRealThis() {
        return this;
    }
}
