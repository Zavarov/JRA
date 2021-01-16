package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.exceptions.HttpException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A query is a POST request with additional arguments. The query acts as a request builder, in order to avoid
 * overloading the call methods and to handle default values.
 * @param <T> The expected return type.
 */
public abstract class Query <T, Q extends Query<T,Q>> {
    /**
     * The client is required for building and executing the query.
     */
    protected final Client client;

    protected final Endpoint endpoint;

    protected final Object[] args;
    /**
     * A map containing all customizable parameters of the query.
     */
    protected final Map<String,Object> params = new HashMap<>();

    protected abstract Q getRealThis();

    public Query(Client client, Endpoint endpoint, Object... args){
        this.client = client;
        this.endpoint = endpoint;
        this.args = args;
    }

    public abstract T query() throws IOException, HttpException, InterruptedException;
}
