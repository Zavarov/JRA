package vartas.jra;

import vartas.jra._json.JSONSubreddit;
import vartas.jra.endpoints.Listings;
import vartas.jra.endpoints.Search;
import vartas.jra.endpoints.Subreddits;
import vartas.jra.endpoints.Users;
import vartas.jra.models.*;
import vartas.jra.query.QueryGet;
import vartas.jra.query.QueryPost;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Subreddit extends SubredditTOP{
    @Override
    public Subreddit getRealThis() {
        return this;
    }

    public static Subreddit from(Client client){
        Subreddit subreddit = new Subreddit();
        subreddit.setClient(client);
        return subreddit;
    }

    public static Subreddit from(Thing thing, Client client){
        assert thing.getKind() == Kind.Subreddit;

        Subreddit subreddit = from(client);
        JSONSubreddit.fromJson(subreddit, thing.getData().toString());
        return subreddit;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //    Listings                                                                                                    //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public Stream<Link> getControversialLinks(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Listings.getControversialLinks(getClient(), Link::from, getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Link> getHotLinks(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Listings.getHotLinks(getClient(), Link::from, getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Link> getNewLinks(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Listings.getNewLinks(getClient(), Link::from, getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Submission getRandomSubmission(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Submission> query = Listings.getRandomSubmission(getClient(), Link::from, getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Stream<Link> getRisingLinks(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Listings.getRisingLinks(getClient(), Link::from, getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Link> getTopLinks(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Listings.getTopLinks(getClient(), Link::from, getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }
    //----------------------------------------------------------------------------------------------------------------//
    //    Search                                                                                                      //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public Listing<Thing> getSearch(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Thing>> query = Search.getSearch(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }
    //----------------------------------------------------------------------------------------------------------------//
    //    Subreddits                                                                                                    //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public Listing<FakeAccount> getBanned(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<FakeAccount>> query = Subreddits.getBanned(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Listing<FakeAccount> getContributors(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<FakeAccount>> query = Subreddits.getContributors(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Stream<FakeAccount> getModerators(Parameter... params) throws InterruptedException, IOException {
        QueryGet<List<FakeAccount>> query = Subreddits.getModerators(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query().stream();
    }

    @Override
    public Listing<FakeAccount> getMuted(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<FakeAccount>> query = Subreddits.getMuted(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Listing<FakeAccount> getWikiBanned(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<FakeAccount>> query = Subreddits.getWikibanned(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Listing<FakeAccount> getWikiContributors(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<FakeAccount>> query = Subreddits.getWikicontributors(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postDeleteBanner(Parameter... params) throws InterruptedException, IOException {
        QueryPost<String> query = Subreddits.postDeleteSubredditBanner(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postDeleteHeader(Parameter... params) throws InterruptedException, IOException {
        QueryPost<String> query = Subreddits.postDeleteSubredditHeader(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postDeleteIcon(Parameter... params) throws InterruptedException, IOException {
        QueryPost<String> query = Subreddits.postDeleteSubredditIcon(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postDeleteImage(Parameter... params) throws InterruptedException, IOException {
        QueryPost<String> query = Subreddits.postDeleteSubredditImage(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postStylesheet(Parameter... params) throws InterruptedException, IOException {
        QueryPost<String> query = Subreddits.postStylesheet(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postUploadImage(Parameter... params) throws InterruptedException, IOException {
        QueryPost<String> query = Subreddits.postUploadImage(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Map<String, Object> getPostRequirements(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Map<String, Object>> query = Subreddits.getPostRequirements(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Map<String, Object> getSubmitText(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Map<String, Object>> query = Subreddits.getSubmitText(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public SubredditSettings getEdit(Parameter... params) throws InterruptedException, IOException {
        QueryGet<SubredditSettings> query = Subreddits.getSubredditEdit(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Rules getRules(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Rules> query = Subreddits.getSubredditRules(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Map<String, Object> getTraffic(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Map<String, Object>> query = Subreddits.getTraffic(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Submission getSticky(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Submission> query = Subreddits.getSticky(getClient(), Link::from);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }
    //----------------------------------------------------------------------------------------------------------------//
    //    Subreddits                                                                                                    //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public String postFriend(Parameter... params) throws InterruptedException, IOException{
        QueryPost<String> query = Users.postFriend(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postSetPermission(Parameter... params) throws InterruptedException, IOException{
        QueryPost<String> query = Users.postSetPermission(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postUnfriend(Parameter... params) throws InterruptedException, IOException{
        QueryPost<String> query = Users.postUnfriend(getClient(), getDisplayName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }
}
