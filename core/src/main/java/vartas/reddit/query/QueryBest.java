package vartas.reddit.query;

public class QueryBest<T> extends QueryThing<T,QueryBest<T>> {
    @Override
    protected QueryBest<T> getRealThis() {
        return this;
    }
}
