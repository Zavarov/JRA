package vartas.jra.query;

import vartas.jra.AbstractClient;
import vartas.jra.endpoints.Endpoint;
import vartas.jra.exceptions.HttpException;
import vartas.jra.http.APIRequest;

import java.io.IOException;
import java.util.function.Function;

public class QueryPatch<Q> extends QueryBase<Q, QueryPatch<Q>>{
    private final APIRequest.BodyType body;

    public QueryPatch(Function<String, Q> mapper, AbstractClient client, Endpoint endpoint, APIRequest.BodyType body, Object... args) {
        super(mapper, client, endpoint, args);
        this.body = body;
    }

    public QueryPatch(Function<String, Q> mapper, AbstractClient client, Endpoint endpoint, Object... args) {
        this(mapper, client, endpoint, APIRequest.BodyType.FORM, args);
    }

    @Override
    protected QueryPatch<Q> getRealThis() {
        return this;
    }

    @Override
    public Q query() throws IOException, HttpException, InterruptedException {
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
