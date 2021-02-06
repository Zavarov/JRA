package vartas.jra.query;

import vartas.jra.Client;
import vartas.jra.Endpoint;
import vartas.jra.exceptions.HttpException;
import vartas.jra.http.APIRequest;

import java.io.IOException;
import java.util.function.Function;

public class QueryPost<Q> extends QueryBase<Q, QueryPost<Q>>{
    private final APIRequest.BodyType body;

    public QueryPost(Function<String, Q> mapper, Client client, Endpoint endpoint, APIRequest.BodyType body, Object... args) {
        super(mapper, client, endpoint, args);
        this.body = body;
    }

    public QueryPost(Function<String, Q> mapper, Client client, Endpoint endpoint, Object... args) {
        this(mapper, client, endpoint, APIRequest.BodyType.FORM, args);
    }

    @Override
    protected QueryPost<Q> getRealThis() {
        return this;
    }

    @Override
    public Q query() throws IOException, HttpException, InterruptedException {
        String source = new APIRequest.Builder(client)
                .setBody(params, body)
                .setEndpoint(endpoint)
                .setArgs(args)
                .build()
                .post();

        System.out.println(source);

        return mapper.apply(source);
    }
}
