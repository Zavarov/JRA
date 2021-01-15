package vartas.reddit.query;

public class QueryTop<T> extends QuerySort<T,QueryTop<T>>{
    @Override
    protected QueryTop<T> getRealThis() {
        return this;
    }
}
