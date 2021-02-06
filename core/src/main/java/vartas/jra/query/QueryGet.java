package vartas.jra.query;

import vartas.jra.Client;
import vartas.jra.Endpoint;
import vartas.jra.exceptions.HttpException;
import vartas.jra.http.APIRequest;

import java.io.IOException;
import java.util.function.Function;

public abstract class QueryGet<T, Q extends QueryBase<T,Q>> extends QueryBase<T, Q>{

    public QueryGet(Function<String, T> mapper, Client client, Endpoint endpoint, Object... args) {
        super(mapper, client, endpoint, args);
    }

    @Override
    public T query() throws IOException, HttpException, InterruptedException{
        String source = new APIRequest.Builder(client)
                .setParams(params)
                .setEndpoint(endpoint)
                .setArgs(args)
                .build()
                .get();

        System.out.println(source);

        return mapper.apply(source);
    }
}
