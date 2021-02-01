package vartas.jra.query;

import vartas.jra.exceptions.HttpException;

import java.io.IOException;

public class QueryInternal<Q> extends Query<Q, QueryInternal<Q>>{
    private final Q value;

    public QueryInternal(Q value){
        super(null, null);
        this.value = value;
    }

    @Override
    protected QueryInternal<Q> getRealThis() {
        return this;
    }

    @Override
    public Q query() throws IOException, HttpException, InterruptedException {
        return value;
    }
}
