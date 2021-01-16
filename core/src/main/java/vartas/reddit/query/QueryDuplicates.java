package vartas.reddit.query;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.Link;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.types.$factory.ListingFactory;
import vartas.reddit.types.Listing;
import vartas.reddit.types.Thing;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class QueryDuplicates extends QueryBase<Pair<Link, List<Link>>,QueryDuplicates> {
    protected static final String CROSSPOSTS_ONLY = "crossposts_only";
    protected static final String SORT = "sort";
    protected static final String SUBREDDIT = "sr";

    public QueryDuplicates(Client client, String article) {
        super(client, Endpoint.GET_DUPLICATES, article);
    }

    @Override
    protected QueryDuplicates getRealThis() {
        return this;
    }

    public QueryDuplicates setCrossPostsOnly(boolean state){
        params.put(CROSSPOSTS_ONLY, state);
        return this;
    }

    public QueryDuplicates setSort(@Nonnull Sort sort){
        params.put(SORT, sort);
        return this;
    }

    public QueryDuplicates setSubreddit(@Nonnull String subreddit){
        params.put(SUBREDDIT, subreddit);
        return this;
    }

    @Override
    public Pair<Link, List<Link>> query() throws IOException, HttpException, InterruptedException {
        JSONArray response = new JSONArray(client.get(params, endpoint, args));
        Link source;
        List<Link> duplicates;

        //We receive an array consisting of two listings.
        //The first listing contains the original submission
        //The second listing contains duplicates. Duplicates are
        //created e.g. by cross-posting the original submission.
        assert response.length() == 2;

        //Extract source
        Thing thing = Thing.from(response.getJSONObject(0));

        Listing listing = ListingFactory.create(Listing::new, thing.getData());
        List<Thing> children = listing.getChildren();

        //Reddit should've only returned a single submission
        assert children.size() == 1;

        source = Thing.toLink(children.get(0));

        //Duplicates, if present
        thing = Thing.from(response.getJSONObject(1));

        listing = ListingFactory.create(Listing::new, thing.getData());
        children = listing.getChildren();

        duplicates = children.stream().map(Thing::toLink).collect(Collectors.toUnmodifiableList());

        return new ImmutablePair<>(source, duplicates);
    }

    public enum Sort{
        NUMBER_OF_COMMENTS("num_comments"),
        NEW("new");

        private final String name;
        Sort(String name){
            this.name = name;
        }

        @Override
        public String toString(){
            return name;
        }
    }
}
