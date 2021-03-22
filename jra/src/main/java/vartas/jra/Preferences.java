package vartas.jra;

import vartas.jra.endpoints.Account;
import vartas.jra.models.FakeAccount;
import vartas.jra.models.Messaging;
import vartas.jra.query.QueryGet;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class Preferences extends PreferencesTOP{
    @Override
    public Preferences getRealThis() {
        return this;
    }

    public static Preferences from(Client client){
        Preferences preferences = new Preferences();
        preferences.setClient(client);
        return preferences;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //    Account                                                                                                     //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public Stream<FakeAccount> getBlocked(Parameter... params) throws InterruptedException, IOException {
        QueryGet<List<FakeAccount>> query = Account.getPreferencesBlocked(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query().stream();
    }

    @Override
    public Stream<FakeAccount> getFriends(Parameter... params) throws InterruptedException, IOException {
        QueryGet<List<FakeAccount>> query = Account.getPreferencesFriends(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query().stream();
    }

    @Override
    public Messaging getMessaging(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Messaging> query = Account.getPreferencesMessaging(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Stream<FakeAccount> getTrusted(Parameter... params) throws InterruptedException, IOException {
        QueryGet<List<FakeAccount>> query = Account.getPreferencesTrusted(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query().stream();
    }
}
