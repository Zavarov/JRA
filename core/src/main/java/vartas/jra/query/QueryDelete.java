package vartas.jra.query;

import vartas.jra.Client;
import vartas.jra.Endpoint;
import vartas.jra.exceptions.HttpException;
import vartas.jra.http.APIRequest;

import java.io.IOException;
import java.util.function.Function;

public class QueryDelete<Q> extends QueryBase<Q, QueryDelete<Q>>{

    public QueryDelete(Function<String, Q> mapper, Client client, Endpoint endpoint, Object... args) {
        super(mapper, client, endpoint, args);
    }

    @Override
    protected QueryDelete<Q> getRealThis() {
        return this;
    }

    @Override
    public Q query() throws IOException, HttpException, InterruptedException {
        String source = new APIRequest.Builder(client)
                .setBody(params)
                .setEndpoint(endpoint)
                .setArgs(args)
                .build()
                .delete();

        return mapper.apply(source);
    }
}
