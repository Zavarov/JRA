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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QueryComments extends Query<Pair<Link,List<Thing>>, QueryComments> {
    private static final String COMMENT = "comment";
    private static final String CONTEXT = "context";
    private static final String DEPTH = "depth";
    private static final String LIMIT = "limit";
    private static final String SHOW_EDITS = "showedits";
    private static final String SHOW_MEDIA = "showmedia";
    private static final String SHOW_MORE = "showmore";
    private static final String SORT = "sort";
    private static final String EXPAND_SUBREDDITS = "sr_detail";
    private static final String THREADED = "threaded";
    private static final String TRUNCATE = "truncate";

    public QueryComments(Client client, Endpoint endpoint, Object... args) {
        super(client, endpoint, args);
    }

    @Override
    protected QueryComments getRealThis() {
        return this;
    }

    public QueryComments setComment(@Nonnull String comment){
        params.put(COMMENT, comment);
        return this;
    }

    public QueryComments setContext(int context){
        assert context >= 0 && context <= 8;
        params.put(CONTEXT, context);
        return this;
    }

    public QueryComments setDepth(int depth){
        params.put(DEPTH, depth);
        return this;
    }

    public QueryComments setLimit(int limit){
        params.put(LIMIT, limit);
        return this;
    }

    public QueryComments setShowEdits(boolean state){
        params.put(SHOW_EDITS, state);
        return this;
    }

    public QueryComments setShowMedia(boolean state){
        params.put(SHOW_MEDIA, state);
        return this;
    }

    public QueryComments setShowMore(boolean state){
        params.put(SHOW_MORE, state);
        return this;
    }

    public QueryComments setSort(@Nonnull Sort sort){
        params.put(SORT, sort);
        return this;
    }

    public QueryComments setExpandSubreddit(boolean state){
        params.put(EXPAND_SUBREDDITS, state);
        return this;
    }

    public QueryComments setThreaded(boolean state){
        params.put(THREADED, state);
        return this;
    }

    public QueryComments setTruncate(int amount){
        assert amount >= 0 && amount <= 50;
        params.put(TRUNCATE, amount);
        return this;
    }

    @Override
    public Pair<Link, List<Thing>> query() throws InterruptedException, IOException, HttpException {
        JSONArray response = new JSONArray(client.get(params, endpoint, args));
        Link link;
        List<Thing> comments;

        //We receive an array consisting of two listings.
        //The first listing contains a randomly fetched submission
        //The second listing contains comments belonging to the fetched submission
        assert response.length() == 2;

        //Extract random submissions
        Thing thing = Thing.from(response.getJSONObject(0));

        Listing listing = ListingFactory.create(Listing::new, thing.getData());
        List<Thing> children = listing.getChildren();

        //Reddit should've only returned a single submission
        assert children.size() == 1;

        link = Thing.toLink(children.get(0));

        //Extract comments, if present

        thing = Thing.from(response.getJSONObject(1));

        listing = ListingFactory.create(Listing::new, thing.getData());
        comments = Collections.unmodifiableList(listing.getChildren());

        return new ImmutablePair<>(link, comments);
    }

    public enum Sort{
        CONFIDENCE,
        TOP,
        NEW,
        CONTROVERSIAL,
        OLD,
        RANDOM,
        QA,
        LIVE;

        @Override
        public String toString(){
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
