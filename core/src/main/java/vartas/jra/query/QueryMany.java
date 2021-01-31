package vartas.jra.query;

import vartas.jra.Client;
import vartas.jra.Endpoint;
import vartas.jra.types.Thing;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class QueryMany<T> extends QueryGet<List<T>, QueryMany<T>> {
    public QueryMany(Function<Thing, T> mapper, Client client, Endpoint endpoint, Object... args) {
        super(many(mapper), client, endpoint, args);
    }

    private static <Q> Function<String, List<Q>> many(Function<Thing, Q> source){
        return value -> Thing.from(value)
                .toListing()
                .getChildren()
                .stream()
                .map(source)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    protected QueryMany<T> getRealThis() {
        return this;
    }
}
