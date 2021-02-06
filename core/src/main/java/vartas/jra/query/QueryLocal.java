package vartas.jra.query;

import vartas.jra.exceptions.HttpException;

import java.io.IOException;

public class QueryLocal<Q> extends Query<Q, QueryLocal<Q>>{
    private final Q value;

    public QueryLocal(Q value){
        super(null, null);
        this.value = value;
    }

    @Override
    protected QueryLocal<Q> getRealThis() {
        return this;
    }

    @Override
    public Q query() throws IOException, HttpException, InterruptedException {
        return value;
    }
}
