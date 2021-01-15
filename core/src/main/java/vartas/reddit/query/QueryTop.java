package vartas.reddit.query;

public class QueryTop extends QuerySort<QueryTop>{
    @Override
    protected QueryTop getRealThis() {
        return this;
    }
}
