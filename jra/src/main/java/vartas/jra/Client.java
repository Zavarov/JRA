package vartas.jra;

import vartas.jra.endpoints.Account;
import vartas.jra.endpoints.Listings;
import vartas.jra.endpoints.Subreddits;
import vartas.jra.endpoints.Users;
import vartas.jra.exceptions.HttpException;
import vartas.jra.models.*;
import vartas.jra.query.QueryGet;
import vartas.jra.query.QueryPost;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class Client extends ClientTOP{
    @Override
    @Nonnull
    public Client getRealThis() {
        return this;
    }

    @Override
    public FrontPage getFrontPage() {
        return FrontPage.from(this);
    }

    //----------------------------------------------------------------------------------------------------------------//
    //    Account                                                                                                     //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public SelfAccount getMe(Parameter... params) throws InterruptedException, IOException, HttpException {
        Supplier<SelfAccount> supplier = () -> SelfAccount.from(this);
        QueryGet<SelfAccount> query = Account.getMe(this, supplier);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }
    //----------------------------------------------------------------------------------------------------------------//
    //    Listings                                                                                                    //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public TrendingSubreddits getTrendingSubreddits(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<TrendingSubreddits> query = Listings.getTrendingSubreddits(this);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Stream<Link> getLinksById(String[] names, Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Listing<Link>> query = Listings.getLinksById(this, Link::from, names);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Submission getComments(String article, Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Submission> query = Listings.getComments(this, Link::from, article);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Duplicate getDuplicates(String article, Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Duplicate> query = Listings.getDuplicates(this, Link::from, article);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }
    //----------------------------------------------------------------------------------------------------------------//
    //    Subreddits                                                                                                    //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public Stream<String> getSearchRedditNames(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<List<String>> query = Subreddits.getSearchRedditNames(this);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query().stream();
    }

    @Override
    public Stream<String> postSearchRedditNames(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryPost<List<String>> query = Subreddits.postSearchRedditNames(this);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query().stream();
    }

    @Override
    public Stream<FakeSubreddit> postSearchSubreddits(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryPost<List<FakeSubreddit>> query = Subreddits.postSearchSubreddits(this);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query().stream();
    }

    @Override
    public String postSiteAdmin(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryPost<String> query = Subreddits.postSiteAdmin(this);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Stream<FakeSubreddit> getSubredditAutocomplete(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<List<FakeSubreddit>> query = Subreddits.getSubredditAutocomplete(this);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query().stream();
    }

    @Override
    public Listing<Thing> getSubredditAutocompleteV2(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Listing<Thing>> query = Subreddits.getSubredditAutocompleteV2(this);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postSubscribe(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryPost<String> query = Subreddits.postSubscribe(this);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Subreddit getSubreddit(String name, Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Subreddit> query = Subreddits.getSubreddit(this, () -> Subreddit.from(this), name);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Stream<Subreddit> getSubredditsDefault(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getSubredditsDefault(this, (thing) -> Subreddit.from(thing, this));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Subreddit> getSubredditsGold(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getSubredditsGold(this, (thing) -> Subreddit.from(thing, this));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Subreddit> getSubredditsNew(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getSubredditsNew(this, (thing) -> Subreddit.from(thing, this));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Subreddit> getSubredditsPopular(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getSubredditsPopular(this, (thing) -> Subreddit.from(thing, this));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Subreddit> getSubredditsSearch(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getSubredditsSearch(this, (thing) -> Subreddit.from(thing, this));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Subreddit> getUsersNew(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getUsersNew(this, (thing) -> Subreddit.from(thing, this));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Subreddit> getUsersPopular(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getUsersPopular(this, (thing) -> Subreddit.from(thing, this));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Subreddit> getUsersSearch(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Listing<Subreddit>> query = Subreddits.getUsersSearch(this, (thing) -> Subreddit.from(thing, this));

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }
    //----------------------------------------------------------------------------------------------------------------//
    //    Users                                                                                                       //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public Map<String, FakeAccount> getUserDataByAccountIds(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Map<String, FakeAccount>> query = Users.getUserDataByAccountIds(this);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public boolean getUsernameAvailable(Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<Boolean> query = Users.getUsernameAvailable(this);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public vartas.jra.Account getAccount(String name, Parameter... params) throws InterruptedException, IOException, HttpException {
        QueryGet<vartas.jra.Account> query = Users.getAccount(this, () -> vartas.jra.Account.from(this), name);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }
}
