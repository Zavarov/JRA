package vartas.reddit.query.listings;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.Link;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.http.APIRequest;
import vartas.reddit.query.Query;
import vartas.reddit.types.Listing;
import vartas.reddit.types.Thing;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QueryComment extends Query<Pair<Link,List<Thing>>, QueryComment> {
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

    public QueryComment(Client client, Endpoint endpoint, Object... args) {
        super(client, endpoint, args);
    }

    @Override
    protected QueryComment getRealThis() {
        return this;
    }

    public QueryComment setComment(@Nonnull String comment){
        params.put(COMMENT, comment);
        return this;
    }

    public QueryComment setContext(int context){
        assert context >= 0 && context <= 8;
        params.put(CONTEXT, context);
        return this;
    }

    public QueryComment setDepth(int depth){
        params.put(DEPTH, depth);
        return this;
    }

    public QueryComment setLimit(int limit){
        params.put(LIMIT, limit);
        return this;
    }

    public QueryComment setShowEdits(boolean state){
        params.put(SHOW_EDITS, state);
        return this;
    }

    public QueryComment setShowMedia(boolean state){
        params.put(SHOW_MEDIA, state);
        return this;
    }

    public QueryComment setShowMore(boolean state){
        params.put(SHOW_MORE, state);
        return this;
    }

    public QueryComment setSort(@Nonnull Sort sort){
        params.put(SORT, sort);
        return this;
    }

    public QueryComment setExpandSubreddit(boolean state){
        params.put(EXPAND_SUBREDDITS, state);
        return this;
    }

    public QueryComment setThreaded(boolean state){
        params.put(THREADED, state);
        return this;
    }

    public QueryComment setTruncate(int amount){
        assert amount >= 0 && amount <= 50;
        params.put(TRUNCATE, amount);
        return this;
    }

    @Override
    public Pair<Link, List<Thing>> query() throws InterruptedException, IOException, HttpException {
        String source = new APIRequest.Builder(client)
                .setParams(params)
                .setEndpoint(endpoint)
                .setArgs(args)
                .build()
                .get();
        JSONArray response = new JSONArray(source);
        Link link;
        List<Thing> comments;

        //We receive an array consisting of two listings.
        //The first listing contains a randomly fetched submission
        //The second listing contains comments belonging to the fetched submission
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
