package vartas.jra.query;

import vartas.jra.AbstractClient;
import vartas.jra.endpoints.Endpoint;
import vartas.jra.http.APIRequest;

import java.io.IOException;
import java.util.function.Function;

public class QueryPut<Q> extends QueryBase<Q, QueryPut<Q>>{
    private final APIRequest.BodyType body;

    public QueryPut(Function<String, Q> mapper, AbstractClient client, Endpoint endpoint, APIRequest.BodyType body, Object... args) {
        super(mapper, client, endpoint, args);
        this.body = body;
    }

    public QueryPut(Function<String, Q> mapper, AbstractClient client, Endpoint endpoint, Object... args) {
        this(mapper, client, endpoint, APIRequest.BodyType.FORM, args);
    }

    @Override
    protected QueryPut<Q> getRealThis() {
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
                .put();

        return mapper.apply(source);
    }
}
