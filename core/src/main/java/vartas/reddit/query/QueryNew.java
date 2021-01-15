package vartas.reddit.query;

public class QueryNew<T> extends QueryThing<T, QueryNew<T>> {
    @Override
    protected QueryNew<T> getRealThis() {
        return this;
    }
}
