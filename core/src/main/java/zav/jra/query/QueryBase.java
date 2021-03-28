package zav.jra.query;

import zav.jra.AbstractClient;
import zav.jra.endpoints.Endpoint;

import java.util.function.Function;

public abstract class QueryBase<T, Q extends Query<T,Q>> extends Query<T, Q>{
    protected final Function<String, T> mapper;

    public QueryBase(Function<String, T> mapper, AbstractClient client, Endpoint endpoint, Object... args) {
        super(client, endpoint, args);
        this.mapper = mapper;
    }
}
