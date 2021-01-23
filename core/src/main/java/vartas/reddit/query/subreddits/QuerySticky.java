package vartas.reddit.query.subreddits;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.Link;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.exceptions.NotFoundException;
import vartas.reddit.http.APIRequest;
import vartas.reddit.query.Query;
import vartas.reddit.types.Listing;
import vartas.reddit.types.Thing;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class QuerySticky extends Query<Optional<Pair<Link, List<Thing>>>, QuerySticky> {
    protected static final String INDEX = "num";

    public QuerySticky(Client client, String subreddit) {
        super(client, Endpoint.GET_SUBREDDIT_ABOUT_STICKY, subreddit);
    }

    @Override
    protected QuerySticky getRealThis() {
        return this;
    }

    public QuerySticky setIndex(int index){
        //Reddit only allows at most two submissions to be stickied
        assert index == 1 || index == 2;
        params.put(INDEX, index);
        return this;
    }

    @Override
    public Optional<Pair<Link, List<Thing>>> query() throws IOException, HttpException, InterruptedException {
        assert params.containsKey(INDEX);

        JSONArray response;

        try{
            //Throws 404 if no submissions are stickied
            String source = new APIRequest.Builder(client)
                    .setParams(params)
                    .setEndpoint(endpoint)
                    .setArgs(args)
                    .build()
                    .get();
            response = new JSONArray(source);
        }catch(NotFoundException e){
            return Optional.empty();
        }

        Link link;
        List<Thing> comments;

        //We receive an array consisting of two listings.
        //The first listing contains a randomly fetched submission
        //The second listing contains comments belonging to the fetched submission
        //Note that it might be the case that the comments are compressed
        assert response.length() == 2;

        //Extract random submissions
        Listing listing = Thing.from(response.getJSONObject(0)).toListing();
        List<Thing> children = listing.getChildren();

        //Reddit should've only returned a single submission
        assert children.size() == 1;

        link = children.get(0).toLink();

        //Extract comments, if present
        listing = Thing.from(response.getJSONObject(1)).toListing();
        comments = Collections.unmodifiableList(listing.getChildren());

        return Optional.of(new ImmutablePair<>(link, comments));
    }
}
