package vartas.reddit.query;

public class QueryRising <T> extends QueryThing<T,QueryRising<T>> {
    @Override
    protected QueryRising<T> getRealThis() {
        return this;
    }
}
