package vartas.jra.query;

import vartas.jra.Client;
import vartas.jra.Endpoint;

import java.util.function.Function;

public abstract class QueryBase<T, Q extends Query<T,Q>> extends Query<T, Q>{
    protected final Function<String, T> mapper;

    public QueryBase(Function<String, T> mapper, Client client, Endpoint endpoint, Object... args) {
        super(client, endpoint, args);
        this.mapper = mapper;
    }
}
