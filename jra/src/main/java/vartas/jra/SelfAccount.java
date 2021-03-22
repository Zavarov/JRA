package vartas.jra;

import vartas.jra.endpoints.Account;
import vartas.jra.endpoints.Subreddits;
import vartas.jra.models.Karma;
import vartas.jra.models.Listing;
import vartas.jra.models.Trophy;
import vartas.jra.query.QueryGet;
import vartas.jra.query.QueryPatch;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SelfAccount extends SelfAccountTOP{
    @Override
    public SelfAccount getRealThis() {
        return this;
    }

    public static SelfAccount from(Client client){
        SelfAccount account = new SelfAccount();
        account.setClient(client);
        return account;
    }
    //----------------------------------------------------------------------------------------------------------------//
    //    Account                                                                                                     //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public Stream<Karma> getKarma(Parameter... params) throws InterruptedException, IOException {
        QueryGet<List<Karma>> query = Account.getKarma(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query().stream();
    }

    @Override
    public Preferences getPreferences(Parameter... params) throws InterruptedException, IOException {
        Supplier<Preferences> supplier = () -> Preferences.from(getClient());
        QueryGet<Preferences> query = Account.getPreferences(getClient(), supplier);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Preferences patchPreferences(Parameter... params) throws InterruptedException, IOException {
        Supplier<Preferences> supplier = () -> Preferences.from(getClient());
        QueryPatch<Preferences> query = Account.patchPreferences(getClient(), supplier);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Stream<Trophy> getTrophies(Parameter... params) throws InterruptedException, IOException {
        QueryGet<List<Trophy>> query = Account.getTrophies(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query().stream();
    }
    //----------------------------------------------------------------------------------------------------------------//
    //    Subreddits                                                                                                    //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public Stream<Subreddit> getMineContributor(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getMineContributor(getClient(), (thing) -> Subreddit.from(thing, getClient()));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Subreddit> getMineModerator(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getMineModerator(getClient(), (thing) -> Subreddit.from(thing, getClient()));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Subreddit> getMineStreams(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getMineStreams(getClient(), (thing) -> Subreddit.from(thing, getClient()));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Subreddit> getMineSubscriber(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getMineSubscriber(getClient(), (thing) -> Subreddit.from(thing, getClient()));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }
}
