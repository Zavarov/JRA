package vartas.jra.models;

import vartas.jra.Account;
import vartas.jra.query.QueryOne;

public class FakeAccount extends FakeAccountTOP{
    @Override
    public QueryOne<Account> expand() {
        return getClient().getAccount(getName());
    }

    @Override
    public FakeAccount getRealThis() {
        return this;
    }
}
