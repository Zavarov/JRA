package vartas.reddit.query;

public class QueryControversial<T> extends QuerySort<T,QueryControversial<T>>{
    @Override
    protected QueryControversial<T> getRealThis() {
        return this;
    }
}
