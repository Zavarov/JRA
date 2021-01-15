package vartas.reddit.query;

import org.json.JSONObject;
import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.Query;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.types.$factory.ListingFactory;
import vartas.reddit.types.$factory.ThingFactory;
import vartas.reddit.types.Listing;
import vartas.reddit.types.Thing;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class QueryThing<T, Q extends QueryThing<T,Q>> extends Query<T, Q> {
    /**
     * The full name of the last {@link Thing} before the first element in the query.<p>
     * Default is {@code null}.
     */
    private static final String AFTER = "after";
    /**
     * The full name of the first {@link Thing} after the last element in the query.<p>
     * Default is {@code null}.
     */
    private static final String BEFORE = "before";
    /**
     * A non-negative integer.<p>
     * Default is {@code 0}.
     */
    private static final String COUNT = "count";
    /**
     * The total number of requested things. Has to be a number between [0,100].<p>
     * Default is {@code 25}.
     */
    private static final String LIMIT = "limit";
    /**
     * The String "all".
     */
    private static final String SHOW = "show";
    /**
     * Expand subreddits.<p>
     * Default is {@code false}.
     */
    private static final String EXPAND_SUBREDDIT = "sr_detail";
    /**
     * Is used to transform the thing its corresponding data type.
     */
    private final Function<Thing, T> transformer;
    /**
     * The client is required for building and executing the query.
     */
    private final Client client;

    private final Endpoint endpoint;

    private final Object[] substitute;

    public QueryThing(Function<Thing,T> transformer, Client client, Endpoint endpoint, Object[] substitute){
        this.transformer = transformer;
        this.client = client;
        this.endpoint = endpoint;
        this.substitute = substitute;
    }

    public Q setAfter(@Nullable String after){
        args.put(AFTER, after);
        return getRealThis();
    }

    public Q setBefore(@Nullable String before){
        args.put(BEFORE, before);
        return getRealThis();
    }

    public Q setCount(int count){
        assert count >= 0;
        args.put(COUNT, count);
        return getRealThis();
    }

    public Q setLimit(int limit){
        assert limit >= 0 && limit < 100;
        args.put(LIMIT, limit);
        return getRealThis();
    }

    public Q show(@Nullable String show){
        assert show == null || show.equals("all");
        args.put(SHOW, show);
        return getRealThis();
    }

    public Q expandSubreddits(boolean state){
        args.put(EXPAND_SUBREDDIT, state);
        return getRealThis();
    }

    @Override
    public List<T> query() throws IOException, HttpException, InterruptedException{
        JSONObject response = new JSONObject(client.get(args, endpoint, substitute));

        Thing thing = ThingFactory.create(Thing::new, response);

        //We should've received a collection of things
        assert Thing.Kind.Listing.matches(thing);

        Listing listing = ListingFactory.create(Listing::new, thing.getData());
        List<T> result = new ArrayList<>(listing.getChildren().size());

        for(Thing child : listing.getChildren()) {
            result.add(transformer.apply(child));
        }

        return Collections.unmodifiableList(result);
    }
}
