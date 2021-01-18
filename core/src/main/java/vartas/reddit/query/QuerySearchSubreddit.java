package vartas.reddit.query;

import com.google.common.base.Joiner;
import org.json.JSONObject;
import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.types.Listing;
import vartas.reddit.types.Thing;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class QuerySearchSubreddit extends QuerySort<Thing, QuerySearchSubreddit>{
    /**
     * A string no longer than 5 characters. Setting this parameter may result in an internal server error.
     */
    protected static final String CATEGORY = "category";
    /**
     * Results may contain hits from multiple subreddits.<p>
     * Default is {@code false}.
     */
    protected static final String INCLUDE_FACETS = "include_facets";
    /**
     * A string no longer than 512 characters.
     */
    protected static final String QUERY = "q";
    /**
     * Limit results to this subreddit.<p>
     * Default is {@code true}.
     */
    protected static final String RESTRICT_SUBREDDIT = "restrict_sr";
    /**
     * A list of result types.
     * @see Type
     */
    protected static final String TYPE = "type";

    public QuerySearchSubreddit(Client client, String subreddit) {
        super(Function.identity(), client, Endpoint.GET_SUBREDDIT_SEARCH, subreddit);
        params.put(RESTRICT_SUBREDDIT, true);
        params.put(INCLUDE_FACETS, false);
    }

    @Override
    protected QuerySearchSubreddit getRealThis() {
        return this;
    }

    public QuerySearchSubreddit setCategory(@Nonnull String category){
        assert category.length() <= 5;
        params.put(CATEGORY, category);
        return this;
    }

    public QuerySearchSubreddit includeFacets(boolean state){
        params.put(INCLUDE_FACETS, state);
        return this;
    }

    public QuerySearchSubreddit setQuery(@Nonnull String query){
        assert query.length() <= 512;
        params.put(QUERY, query);
        return this;
    }

    public QuerySearchSubreddit restrictSubreddit(boolean state){
        params.put(RESTRICT_SUBREDDIT, state);
        return this;
    }

    public QuerySearchSubreddit setTypes(@Nonnull Type... types){
        params.put(TYPE, Joiner.on(",").join(types));
        return this;
    }

    public List<Thing> query() throws IOException, HttpException, InterruptedException{
        JSONObject response = new JSONObject(client.get(params, Endpoint.GET_SUBREDDIT_SEARCH, args));

        Listing listing = Thing.from(response).toListing();
        return Collections.unmodifiableList(listing.getChildren());
    }

    public enum Type{
        SUBREDDIT("sr"),
        LINK("link"),
        USER("user");

        private final String name;

        Type(String name){
            this.name = name;
        }

        @Override
        public String toString(){
            return name;
        }
    }

    public enum Sort{
        RELEVANCE,
        HOT,
        TOP,
        NEW,
        COMMENTS;

        @Override
        public String toString(){
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
