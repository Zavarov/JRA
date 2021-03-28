package zav.jra.query;

import zav.jra.AbstractClient;
import zav.jra.endpoints.Endpoint;
import zav.jra.http.APIRequest;

import java.io.IOException;
import java.util.function.Function;

public class QueryPatch<Q> extends QueryBase<Q, QueryPatch<Q>>{
    private final APIRequest.BodyType body;

    public QueryPatch(Function<String, Q> mapper, AbstractClient client, Endpoint endpoint, APIRequest.BodyType body, Object... args) {
        super(mapper, client, endpoint, args);
        this.body = body;
    }

    @Override
    protected QueryPatch<Q> getRealThis() {
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
                .patch();

        return mapper.apply(source);
    }
}
