package vartas.reddit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A query is a POST request with additional arguments. The query acts as a request builder, in order to avoid
 * overloading the call methods and to handle default values.
 * @param <T> The expected return type.
 */
public abstract class Query <T, Q extends Query<T,Q>> {
    /**
     * A map containing all customizable parameters of the query.
     */
    protected final Map<String,Object> args = new HashMap<>();

    protected abstract Q getRealThis();

    public abstract List<T> query();
}
