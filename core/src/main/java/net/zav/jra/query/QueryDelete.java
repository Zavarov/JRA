package net.zav.jra.query;

import net.zav.jra.AbstractClient;
import net.zav.jra.endpoints.Endpoint;
import net.zav.jra.http.APIRequest;

import java.io.IOException;
import java.util.function.Function;

public class QueryDelete<Q> extends QueryBase<Q, QueryDelete<Q>>{
    private final APIRequest.BodyType body;

    public QueryDelete(Function<String, Q> mapper, AbstractClient client, Endpoint endpoint, APIRequest.BodyType body, Object... args) {
        super(mapper, client, endpoint, args);
        this.body = body;
    }

    public QueryDelete(Function<String, Q> mapper, AbstractClient client, Endpoint endpoint, Object... args) {
        this(mapper, client, endpoint, APIRequest.BodyType.FORM, args);
    }

    @Override
    protected QueryDelete<Q> getRealThis() {
        return this;
    }

    @Override
    public Q query() throws IOException, InterruptedException {
        String source = new APIRequest.Builder(client)
                .setHost(host)
                .setBody(params, body)
                .setEndpoint(endpoint)
                .setArgs(args)
                .build()
                .delete();

        return mapper.apply(source);
    }
}
